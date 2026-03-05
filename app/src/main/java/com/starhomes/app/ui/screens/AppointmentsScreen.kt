package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.starhomes.app.data.Appointment
import com.starhomes.app.data.MockData
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800

@Composable
fun AppointmentsScreen(appointments: List<Appointment>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Meus Agendamentos", color = Color.White, fontSize = 22.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        if (appointments.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null,
                    tint = Gray400, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text("Você não possui agendamentos.", color = Gray400)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(appointments) { appointment ->
                    val property = MockData.findProperty(appointment.propertyId) ?: return@items
                    val neighborhood = MockData.findNeighborhoodByProperty(appointment.propertyId)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Gray800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row {
                                AsyncImage(
                                    model = property.image,
                                    contentDescription = property.type,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    // Type badge
                                    val isVisita = appointment.type == "Visita"
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (isVisita) Color(0xFF065F46).copy(alpha = 0.4f) else Color(0xFF4C1D95).copy(alpha = 0.4f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Text(
                                            appointment.type,
                                            color = if (isVisita) Color(0xFF34D399) else Color(0xFFA78BFA),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Text(property.type, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("em ${neighborhood?.name}", color = Gray400, fontSize = 12.sp)
                                }
                            }
                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.08f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(appointment.date, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text(appointment.time, color = Blue400, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
