package com.starhomes.app.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

/**
 * LocationManager: Gerencia o acesso ao sensor de GPS do dispositivo.
 *
 * Usa o FusedLocationProviderClient do Google Play Services para obter
 * a localização real do usuário com alta precisão e baixo consumo de bateria.
 *
 * Sensor utilizado: GPS (ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION)
 */
class LocationManager(private val context: Context) {

    // Cliente de localização fundida (combina GPS, Wi-Fi e rede celular)
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Verifica se as permissões de localização foram concedidas pelo usuário.
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Obtém a localização atual do dispositivo via GPS como um Flow.
     * Emite a Location assim que disponível e então fecha o canal.
     *
     * Requer permissão ACCESS_FINE_LOCATION ou ACCESS_COARSE_LOCATION.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Flow<Location?> = callbackFlow {
        if (!hasLocationPermission()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        // Configuração da requisição de localização
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // Alta precisão (usa GPS)
            5000L // Intervalo de atualização: 5 segundos
        ).apply {
            setMinUpdateIntervalMillis(2000L) // Intervalo mínimo: 2 segundos
            setMaxUpdates(1)                 // Somente 1 leitura necessária
        }.build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                trySend(location)
                fusedLocationClient.removeLocationUpdates(this)
                close()
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    // GPS indisponível — tenta última localização conhecida
                    fusedLocationClient.lastLocation.addOnSuccessListener { last ->
                        trySend(last)
                        close()
                    }.addOnFailureListener {
                        trySend(null)
                        close()
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        // Cancela as atualizações se o Flow for cancelado
        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    /**
     * Converte coordenadas (lat, lng) em um endereço legível usando Geocoder.
     * Exemplo: "Fortaleza, CE, Brasil"
     */
    fun getAddressFromCoordinates(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale("pt", "BR"))
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                buildString {
                    address.thoroughfare?.let { append("$it, ") }
                    address.subLocality?.let { append("$it, ") }
                    address.locality?.let { append("$it") }
                }
            } else {
                "%.4f, %.4f".format(lat, lng)
            }
        } catch (e: Exception) {
            "%.4f, %.4f".format(lat, lng)
        }
    }
}
