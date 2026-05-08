package com.starhomes.app.storage

import android.content.Context
import android.content.SharedPreferences
import com.starhomes.app.data.Appointment
import org.json.JSONArray
import org.json.JSONObject

class StorageManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "star_homes_prefs",  // nome do arquivo de preferências
        Context.MODE_PRIVATE // acesso privado, só este app pode ler
    )



    fun saveFavorites(ids: Set<String>) {
        val jsonArray = JSONArray()
        ids.forEach { jsonArray.put(it) }
        prefs.edit().putString("favorites", jsonArray.toString()).apply()
    }


    fun loadFavorites(): Set<String> {
        val json = prefs.getString("favorites", null) ?: return emptySet()
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { jsonArray.getString(it) }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }


    fun saveAppointments(appointments: List<Appointment>) {
        val jsonArray = JSONArray()
        appointments.forEach { appointment ->
            val obj = JSONObject().apply {
                put("id", appointment.id)
                put("propertyId", appointment.propertyId)
                put("type", appointment.type)
                put("date", appointment.date)
                put("time", appointment.time)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("appointments", jsonArray.toString()).apply()
    }


    fun loadAppointments(): List<Appointment> {
        val json = prefs.getString("appointments", null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).map { index ->
                val obj = jsonArray.getJSONObject(index)
                Appointment(
                    id = obj.getString("id"),
                    propertyId = obj.getString("propertyId"),
                    type = obj.getString("type"),
                    date = obj.getString("date"),
                    time = obj.getString("time")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
