package com.starhomes.app.data

// ----- Enums -----

enum class Screen {
    LOGIN, SIGNUP, PROFILE_SETUP, CHAT,
    SEARCH_RESULTS, NEIGHBORHOOD_DETAILS, PROPERTY_DETAILS,
    EDIT_PROFILE, PREFERENCES_REPORT, PREFERENCES, FAVORITES,
    VIRTUAL_TOUR, SCHEDULE_VISIT, APPOINTMENTS, FORGOT_PASSWORD
}

enum class ProfileType(val label: String) {
    FAMILY("Família"),
    STUDENT("Estudante"),
    PROFESSIONAL("Profissional"),
    OTHER("Outro")
}


data class User(
    val name: String,
    val email: String,
    val phone: String,
    val avatar: String
)

data class Priorities(
    val schools: Boolean,
    val transport: Boolean,
    val safety: Boolean
)

data class Preferences(
    val profileType: ProfileType,
    val priceRange: Pair<Int, Int>,
    val priorities: Priorities
)

data class Room(
    val id: String,
    val name: String,
    val positionX: Float,
    val positionY: Float,
    val image360: String
)

data class FloorPlan(
    val image: String,
    val rooms: List<Room>
)

data class Property(
    val id: String,
    val type: String,
    val bedrooms: Int,
    val price: Int,
    val image: String,
    val lat: Double? = null,
    val lng: Double? = null,
    val floorPlan: FloorPlan? = null
)

data class NeighborhoodCenter(val lat: Double, val lng: Double)

data class Neighborhood(
    val id: String,
    val name: String,
    val description: String,
    val priceRange: String,
    val image: String,
    val center: NeighborhoodCenter,
    val properties: List<Property>
)

data class Appointment(
    val id: String,
    val propertyId: String,
    val type: String,
    val date: String,
    val time: String
)