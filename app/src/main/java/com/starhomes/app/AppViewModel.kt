package com.starhomes.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.starhomes.app.data.Appointment
import com.starhomes.app.data.MockData
import com.starhomes.app.data.User
import com.starhomes.app.storage.StorageManager

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = StorageManager(application)

    var user = mutableStateOf(MockData.USER)
        private set

    var selectedNeighborhoodId = mutableStateOf<String?>(null)
        private set

    var selectedPropertyId = mutableStateOf<String?>(null)
        private set

    val favoritePropertyIds = mutableStateListOf<String>().also { list ->
        list.addAll(storage.loadFavorites())
    }

    val appointments = mutableStateListOf<Appointment>().also { list ->
        list.addAll(storage.loadAppointments())
    }

    fun updateUser(newUser: User) {
        user.value = newUser
    }

    fun selectNeighborhood(id: String) {
        selectedNeighborhoodId.value = id
    }

    fun selectProperty(id: String) {
        selectedPropertyId.value = id
    }


    fun toggleFavorite(propertyId: String) {
        if (favoritePropertyIds.contains(propertyId)) {
            favoritePropertyIds.remove(propertyId)
        } else {
            favoritePropertyIds.add(propertyId)
        }
        // Persiste no armazenamento local
        storage.saveFavorites(favoritePropertyIds.toSet())
    }


    fun addAppointment(propertyId: String, type: String, date: String, time: String) {
        val appointment = Appointment(
            id = "app_${System.currentTimeMillis()}",
            propertyId = propertyId,
            type = type,
            date = date,
            time = time
        )
        appointments.add(appointment)
        appointments.sortBy { it.date }
        // Persiste no armazenamento local
        storage.saveAppointments(appointments.toList())
    }


    fun cancelAppointment(id: String) {
        appointments.removeAll { it.id == id }
        storage.saveAppointments(appointments.toList())
    }
}