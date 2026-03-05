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
            contentDescription = selectedRoom.name,
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
            "360° View",
            color = Color(0xFF9CA3AF),
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
                                    .clickable { selectedRoom = room },
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
    ) {
        Text(room.name, color = Color.White, fontSize = 13.sp)
    }
}
