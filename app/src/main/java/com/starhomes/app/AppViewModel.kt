package com.starhomes.app

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.starhomes.app.data.Appointment
import com.starhomes.app.data.MockData
import com.starhomes.app.data.User

class AppViewModel : ViewModel() {

    var user = mutableStateOf(MockData.USER)
        private set

    var selectedNeighborhoodId = mutableStateOf<String?>(null)
        private set

    var selectedPropertyId = mutableStateOf<String?>(null)
        private set

    val favoritePropertyIds = mutableStateListOf<String>()

    val appointments = mutableStateListOf<Appointment>()

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
    }
}
