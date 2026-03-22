package com.starhomes.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.starhomes.app.data.Screen
import com.starhomes.app.notification.NotificationHelper
import com.starhomes.app.ui.StarHomesTheme
import com.starhomes.app.ui.components.AppFooter
import com.starhomes.app.ui.components.AppHeader
import com.starhomes.app.ui.screens.*

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* permissão concedida ou negada — app funciona nos dois casos */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            StarHomesTheme {
                StarHomesApp()
            }
        }
    }
}

@Composable
fun StarHomesApp(vm: AppViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

    val navigateTo: (Screen) -> Unit = { screen -> currentScreen = screen }

    val handleBack: () -> Unit = {
        currentScreen = when (currentScreen) {
            Screen.SIGNUP -> Screen.LOGIN
            Screen.FORGOT_PASSWORD -> Screen.LOGIN
            Screen.PROFILE_SETUP -> Screen.PREFERENCES_REPORT
            Screen.NEIGHBORHOOD_DETAILS -> Screen.SEARCH_RESULTS
            Screen.PROPERTY_DETAILS -> Screen.NEIGHBORHOOD_DETAILS
            Screen.PREFERENCES_REPORT -> Screen.EDIT_PROFILE
            Screen.FAVORITES -> Screen.SEARCH_RESULTS
            Screen.APPOINTMENTS -> Screen.SEARCH_RESULTS
            Screen.VIRTUAL_TOUR -> Screen.PROPERTY_DETAILS
            Screen.SCHEDULE_VISIT -> Screen.PROPERTY_DETAILS
            else -> currentScreen
        }
    }

    val showHeader = currentScreen != Screen.LOGIN
    val showFooter = currentScreen in listOf(
        Screen.SEARCH_RESULTS, Screen.CHAT, Screen.EDIT_PROFILE,
        Screen.NEIGHBORHOOD_DETAILS, Screen.PROPERTY_DETAILS,
        Screen.FAVORITES, Screen.APPOINTMENTS
    )
    val showBack = currentScreen !in listOf(
        Screen.LOGIN, Screen.SEARCH_RESULTS, Screen.CHAT
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111827))
            .systemBarsPadding()
    ) {
        if (showHeader) {
            AppHeader(showBackButton = showBack, onBack = handleBack)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFF111827))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            when (currentScreen) {
                Screen.LOGIN -> LoginScreen(navigateTo)
                Screen.SIGNUP -> SignUpScreen(navigateTo)
                Screen.PROFILE_SETUP -> ProfileSetupScreen(navigateTo)
                Screen.CHAT -> ChatScreen(navigateTo)
                Screen.SEARCH_RESULTS -> SearchResultsScreen(
                    navigateTo = navigateTo,
                    selectNeighborhood = { vm.selectNeighborhood(it) }
                )
                Screen.NEIGHBORHOOD_DETAILS -> NeighborhoodDetailsScreen(
                    neighborhoodId = vm.selectedNeighborhoodId.value,
                    navigateTo = navigateTo,
                    selectProperty = { vm.selectProperty(it) }
                )
                Screen.PROPERTY_DETAILS -> PropertyDetailsScreen(
                    propertyId = vm.selectedPropertyId.value,
                    isFavorite = vm.favoritePropertyIds.contains(vm.selectedPropertyId.value ?: ""),
                    onToggleFavorite = { vm.toggleFavorite(it) },
                    navigateTo = navigateTo
                )
                Screen.EDIT_PROFILE -> EditProfileScreen(
                    user = vm.user.value,
                    onUpdateUser = { vm.updateUser(it) },
                    navigateTo = navigateTo
                )
                Screen.PREFERENCES_REPORT -> PreferencesReportScreen(navigateTo)
                Screen.FAVORITES -> FavoritesScreen(
                    favoriteIds = vm.favoritePropertyIds.toList(),
                    navigateTo = navigateTo,
                    selectProperty = { vm.selectProperty(it) }
                )
                Screen.VIRTUAL_TOUR -> VirtualTourScreen(propertyId = vm.selectedPropertyId.value)
                Screen.SCHEDULE_VISIT -> ScheduleVisitScreen(
                    propertyId = vm.selectedPropertyId.value,
                    addAppointment = { id, type, date, time -> vm.addAppointment(id, type, date, time) },
                    navigateTo = navigateTo
                )
                Screen.APPOINTMENTS -> AppointmentsScreen(
                    appointments = vm.appointments.toList(),
                    onCancelAppointment = { id -> vm.cancelAppointment(id) }
                )
                Screen.FORGOT_PASSWORD -> ForgotPasswordScreen(navigateTo)
                else -> {}
            }
        }

        if (showFooter) {
            AppFooter(activeScreen = currentScreen, navigateTo = navigateTo)
        }
    }
}