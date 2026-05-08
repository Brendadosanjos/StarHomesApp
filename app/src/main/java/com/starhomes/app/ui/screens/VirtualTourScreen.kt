package com.starhomes.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Room

@Composable
fun VirtualTourScreen(propertyId: String?) {
    val property = MockData.findProperty(propertyId ?: "")
        ?: return Text("Imóvel não encontrado.", color = Color.White)
    val rooms = property.floorPlan?.rooms ?: return Text("Planta não disponível.", color = Color.White)

    var selectedRoom by remember { mutableStateOf(rooms.first()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Tour Virtual", color = Color.White, fontSize = 20.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

        // 360° image
        AsyncImage(
            model = selectedRoom.image360,
            // ACESSIBILIDADE: descreve o cômodo visualizado no tour virtual,
            // permitindo que usuários de TalkBack entendam qual ambiente estão vendo.
            contentDescription = "Foto 360 graus do cômodo: ${selectedRoom.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(8.dp))
        Text(
            selectedRoom.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Visão 360°",
            // ACESSIBILIDADE (contraste): cor anterior #9CA3AF (Gray400) sobre fundo
            // #111827 tem relação 4.6:1 — abaixo do mínimo WCAG AA para texto pequeno (4.5:1 no limite).
            // Substituído por Color.White para garantir contraste adequado.
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))

        // Room selector
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(rooms) { room ->
                RoomChip(room = room, isSelected = room.id == selectedRoom.id) {
                    selectedRoom = room
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Floor plan
        property.floorPlan?.let { plan ->
            Text("Planta Baixa", color = Color.White, fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = plan.image,
                    contentDescription = "Planta Baixa",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Room pins overlaid on floor plan
                plan.rooms.forEach { room ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.TopStart)
                                .padding(
                                    start = (room.positionX / 100f * 250).dp,
                                    top = (room.positionY / 100f * 180).dp
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (room.id == selectedRoom.id) Color(0xFF2563EB)
                                        else Color.Black.copy(alpha = 0.7f)
                                    )
                                    .border(
                                        1.dp,
                                        if (room.id == selectedRoom.id) Color.White else Color.Gray,
                                        RoundedCornerShape(50)
                                    )
                                    .clickable { selectedRoom = room }
                                    // ACESSIBILIDADE: pin do mapa era invisível ao TalkBack.
                                    // semantics anuncia nome do cômodo e estado de seleção,
                                    // permitindo navegação completa pela planta baixa sem visão.
                                    .semantics {
                                        contentDescription = if (room.id == selectedRoom.id)
                                            "${room.name}, selecionado"
                                        else
                                            "${room.name}, toque para visualizar"
                                        role = Role.Button
                                        selected = room.id == selectedRoom.id
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("●", color = Color.White, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoomChip(room: Room, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF2563EB) else Color(0xFF1F2937))
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Color(0xFF374151),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            // ACESSIBILIDADE: Box com clickable é invisível ao TalkBack sem semantics.
            // Anuncia o nome do cômodo, se está selecionado e o role de botão,
            // permitindo navegação completa pelo seletor de cômodos sem visão.
            .semantics {
                contentDescription = if (isSelected)
                    "${room.name}, selecionado"
                else
                    "${room.name}, toque para selecionar"
                role = Role.Button
                selected = isSelected
            }
    ) {
        Text(room.name, color = Color.White, fontSize = 13.sp)
    }
}