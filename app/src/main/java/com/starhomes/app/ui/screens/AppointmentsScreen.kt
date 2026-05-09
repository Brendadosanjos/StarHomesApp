package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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
fun AppointmentsScreen(
    appointments: List<Appointment>,
    onCancelAppointment: (String) -> Unit = {}
) {
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Meus Agendamentos",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (appointments.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.DateRange,
                    // ACESSIBILIDADE: ícone ilustrativo do estado vazio — a descrição
                    // está no texto abaixo, por isso mantemos null aqui para evitar
                    // duplicidade na leitura do TalkBack.
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Você não possui agendamentos.",
                    color = Gray400,
                    // ACESSIBILIDADE: semantics garante que o TalkBack leia esta
                    // mensagem de estado vazio como uma região de status.
                    modifier = Modifier.semantics {
                        contentDescription = "Lista vazia. Você não possui agendamentos."
                    }
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(appointments, key = { it.id }) { appointment ->
                    val property = MockData.findProperty(appointment.propertyId) ?: return@items
                    val neighborhood = MockData.findNeighborhoodByProperty(appointment.propertyId)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Gray800),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = property.image,
                                    contentDescription = "Foto do imóvel: ${property.type} em ${neighborhood?.name ?: "bairro"}",
                                    contentScale = ContentScale.Crop,
                                    // OTIMIZAÇÃO 1 aplicada: placeholder evita layout
                                    // shift enquanto a imagem carrega; error evita
                                    // espaço vazio se o download falhar.
                                    placeholder = rememberVectorPainter(Icons.Default.Home),
                                    error = rememberVectorPainter(Icons.Default.BrokenImage),
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    // Badge do tipo
                                    val isVisita = appointment.type == "Visita"
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (isVisita)
                                            Color(0xFF065F46).copy(alpha = 0.4f)
                                        else
                                            Color(0xFF4C1D95).copy(alpha = 0.4f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    ) {
                                        Text(
                                            text = appointment.type,
                                            color = if (isVisita) Color(0xFF34D399) else Color(0xFFA78BFA),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Text(
                                        text = property.type,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "em ${neighborhood?.name}",
                                        color = Gray400,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.08f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = appointment.date,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = appointment.time,
                                        color = Blue400,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { appointmentToCancel = appointment },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFF87171)
                                    ),
                                    // CORREÇÃO LINT: outlinedButtonBorder (sem parâmetro enabled)
                                    // foi depreciado. Substituído por BorderStroke direto,
                                    // que é a forma recomendada e não depreciada.
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = 1.dp,
                                        color = Color(0xFFF87171).copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.semantics {
                                        contentDescription =
                                            "Cancelar agendamento de ${appointment.type} em ${appointment.date} às ${appointment.time}"
                                        role = Role.Button
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        // ACESSIBILIDADE: null porque o semantics do botão
                                        // já descreve a ação completa — evita leitura dupla.
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(text = "Cancelar", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    appointmentToCancel?.let { appointment ->
        AlertDialog(
            onDismissRequest = { appointmentToCancel = null },
            containerColor = Color(0xFF1F2937),
            title = {
                Text(
                    text = "Cancelar agendamento?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Tem certeza que deseja cancelar a visita agendada para ${appointment.date} às ${appointment.time}?",
                    color = Gray400
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelAppointment(appointment.id)
                        appointmentToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7F1D1D).copy(alpha = 0.8f)
                    ),
                    // ACESSIBILIDADE: semantics do botão de confirmação descreve
                    // a ação destrutiva de forma clara para o TalkBack.
                    modifier = Modifier.semantics {
                        contentDescription = "Confirmar cancelamento do agendamento"
                    }
                ) {
                    Text(text = "Sim, cancelar", color = Color(0xFFF87171))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { appointmentToCancel = null },
                    modifier = Modifier.semantics {
                        contentDescription = "Voltar, manter agendamento"
                    }
                ) {
                    Text(text = "Voltar", color = Gray400)
                }
            }
        )
    }
}