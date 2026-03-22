package com.starhomes.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.notification.NotificationHelper
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Blue600
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray700
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import java.text.SimpleDateFormat
import java.util.*

private val AVAILABLE_TIME_SLOTS = listOf(
    "08:00", "08:30", "09:00", "09:30",
    "10:00", "10:30", "11:00", "11:30",
    "13:00", "13:30", "14:00", "14:30",
    "15:00", "15:30", "16:00", "16:30",
    "17:00", "17:30"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleVisitScreen(
    propertyId: String?,
    addAppointment: (String, String, String, String) -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val context = LocalContext.current
    val property = MockData.findProperty(propertyId ?: "")
        ?: return Text("Imóvel não encontrado.", color = Color.White)
    val neighborhood = MockData.findNeighborhoodByProperty(property.id)

    var displayDate by remember { mutableStateOf("") }      // DD/MM/AAAA
    var saveDate by remember { mutableStateOf("") }         // AAAA-MM-DD
    var selectedTime by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                return utcTimeMillis >= today
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Agendar Visita",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )

        // Card do imóvel
        Card(
            colors = CardDefaults.cardColors(containerColor = Gray800),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                AsyncImage(
                    model = property.image,
                    contentDescription = property.type,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(property.type, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("em ${neighborhood?.name}", color = Gray400, fontSize = 13.sp)
                    Text(
                        "£${property.price}/mês",
                        color = Blue400,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Text("Data da visita", color = Gray400, fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp))

        OutlinedCard(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF1F2937)),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (displayDate.isNotEmpty()) displayDate else "Selecione uma data",
                    color = if (displayDate.isNotEmpty()) Color.White else Gray400,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Blue400)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Horário da visita", color = Gray400, fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp))

        OutlinedCard(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFF1F2937)),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedTime.isNotEmpty()) selectedTime else "Selecione um horário",
                    color = if (selectedTime.isNotEmpty()) Color.White else Gray400,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Blue400)
            }
        }

        if (showError) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Por favor, selecione a data e o horário.",
                color = Color(0xFFF87171),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(32.dp))

        PrimaryButton(
            text = "Confirmar Agendamento",
            onClick = {
                if (saveDate.isNotBlank() && selectedTime.isNotBlank()) {
                    showError = false
                    addAppointment(property.id, "Visita", displayDate, selectedTime)
                    NotificationHelper.sendAppointmentNotification(
                        context = context,
                        property = property.type,
                        date = displayDate,
                        time = selectedTime
                    )
                    navigateTo(Screen.APPOINTMENTS)
                } else {
                    showError = true
                }
            }
        )
        Spacer(Modifier.height(16.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Formato de exibição: DD/MM/AAAA
                        val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                        // Formato de salvamento: AAAA-MM-DD
                        val sdfSave = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
                        val date = Date(millis)
                        displayDate = sdfDisplay.format(date)
                        saveDate = sdfSave.format(date)
                    }
                    showDatePicker = false
                }) {
                    Text("Confirmar", color = Blue400)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Gray400)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF1F2937)
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFF1F2937),
                    titleContentColor = Color.White,
                    headlineContentColor = Blue400,
                    weekdayContentColor = Gray400,
                    subheadContentColor = Color.White,
                    navigationContentColor = Color.White,
                    yearContentColor = Color.White,
                    currentYearContentColor = Blue400,
                    selectedYearContainerColor = Blue600,
                    selectedYearContentColor = Color.White,
                    dayContentColor = Color.White,
                    selectedDayContainerColor = Blue600,
                    selectedDayContentColor = Color.White,
                    todayContentColor = Blue400,
                    todayDateBorderColor = Blue400,
                    disabledDayContentColor = Gray700
                )
            )
        }
    }

    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Selecione o horário",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        "Segunda a Sábado",
                        color = Gray400,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    // Lista rolável de horários
                    val listState = rememberLazyListState()
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    ) {
                        items(AVAILABLE_TIME_SLOTS) { slot ->
                            val isSelected = selectedTime == slot
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedTime = slot
                                        showTimePicker = false
                                    }
                                    .background(
                                        if (isSelected) Blue600.copy(alpha = 0.3f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 8.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = slot,
                                    color = if (isSelected) Blue400 else Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Text("✓", color = Blue400, fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = { showTimePicker = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancelar", color = Gray400)
                    }
                }
            }
        }
    }
}