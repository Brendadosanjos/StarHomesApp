package com.starhomes.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleVisitScreen(
    propertyId: String?,
    addAppointment: (String, String, String, String) -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val property = MockData.findProperty(propertyId ?: "")
        ?: return Text("Imóvel não encontrado.", color = Color.White)
    val neighborhood = MockData.findNeighborhoodByProperty(property.id)

    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    // DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // TimePicker
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(is24Hour = true)

    // DatePicker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        date = sdf.format(Date(millis))
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
                containerColor = Gray800
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Gray800,
                    titleContentColor = Color.White,
                    headlineContentColor = Color.White,
                    weekdayContentColor = Gray400,
                    subheadContentColor = Color.White,
                    navigationContentColor = Color.White,
                    yearContentColor = Color.White,
                    currentYearContentColor = Blue400,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Blue400,
                    dayContentColor = Color.White,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Blue400,
                    todayContentColor = Blue400,
                    todayDateBorderColor = Blue400
                )
            )
        }
    }

    // TimePicker Dialog
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            containerColor = Gray800,
            title = {
                Text("Escolha o horário", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFF1F2937),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color.White,
                        selectorColor = Blue400,
                        containerColor = Gray800,
                        timeSelectorSelectedContainerColor = Blue400,
                        timeSelectorUnselectedContainerColor = Color(0xFF1F2937),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Gray400
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val h = timePickerState.hour.toString().padStart(2, '0')
                    val m = timePickerState.minute.toString().padStart(2, '0')
                    time = "$h:$m"
                    showTimePicker = false
                }) {
                    Text("Confirmar", color = Blue400)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar", color = Gray400)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Agendar Visita", color = Color.White, fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // Property card
        Card(
            colors = CardDefaults.cardColors(containerColor = Gray800),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                AsyncImage(
                    model = property.image,
                    contentDescription = property.type,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(property.type, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("em ${neighborhood?.name}", color = Gray400, fontSize = 12.sp)
                    Text(
                        "£${property.price}/mês", color = Blue400,
                        fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Date picker field
        Text("Data", color = Gray400, fontSize = 13.sp, modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF374151), RoundedCornerShape(10.dp))
                .clickable { showDatePicker = true }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (date.isEmpty()) "dd/mm/aaaa" else date,
                    color = if (date.isEmpty()) Gray400 else Color.White,
                    fontSize = 15.sp
                )
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Escolher data",
                    tint = Blue400,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // Time picker field
        Text("Horário", color = Gray400, fontSize = 13.sp, modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF374151), RoundedCornerShape(10.dp))
                .clickable { showTimePicker = true }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (time.isEmpty()) "--:--" else time,
                    color = if (time.isEmpty()) Gray400 else Color.White,
                    fontSize = 15.sp
                )
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Escolher horário",
                    tint = Blue400,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Confirmar Agendamento",
            onClick = {
                if (date.isNotBlank() && time.isNotBlank()) {
                    addAppointment(property.id, "Visita", date, time)
                    navigateTo(Screen.APPOINTMENTS)
                }
            }
        )
    }
}