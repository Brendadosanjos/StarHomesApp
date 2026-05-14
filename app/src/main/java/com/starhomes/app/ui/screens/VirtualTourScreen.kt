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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
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
        // ACESSIBILIDADE: contentDescription = null porque o Text com
        // selectedRoom.name logo abaixo já anuncia o cômodo ao TalkBack.
        // Ter os dois gerava "Sala de Estar identical to 2 other items".
        AsyncImage(
            model = selectedRoom.image360,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(8.dp))
        // ACESSIBILIDADE: Column com clearAndSetSemantics agrupa nome + "Visão 360°"
        // numa leitura única: "Sala de Estar, Visão 360 graus".
        // Sem isso, o Text do nome era focado separadamente do chip e da imagem,
        // contribuindo para o "Sala de Estar identical to 2 other items".
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {
                    contentDescription = "${selectedRoom.name}, Visão 360 graus"
                }
        ) {
            Text(
                selectedRoom.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "Visão 360°",
                color = Color.White,
                fontSize = 12.sp
            )
        }
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
                    // ACESSIBILIDADE: o Text "Planta Baixa" acima já anuncia o contexto.
                    // A imagem com contentDescription = "Planta Baixa" duplicava o texto,
                    // gerando "Item descriptions — 'Planta Baixa' identical to 1 other item".
                    // null aqui evita a duplicação — o TalkBack já leu o título da seção.
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Room pins overlaid on floor plan
                plan.rooms.forEach { room ->
                    Box(modifier = Modifier.fillMaxSize()) {
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
                                    // ACESSIBILIDADE: o Text("●") era idêntico em todos os
                                    // pins, gerando "'●' identical to 4 other items".
                                    // clearAndSetSemantics substitui o "●" por uma descrição
                                    // única por pin: "Sala de Estar, selecionado" ou
                                    // "Cozinha, toque para visualizar".
                                    .clearAndSetSemantics {
                                        contentDescription = if (room.id == selectedRoom.id)
                                            "${room.name}, selecionado na planta baixa"
                                        else
                                            "${room.name}, toque para visualizar"
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                // Text decorativo — descrição já está no clearAndSetSemantics
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
            // ACESSIBILIDADE: o Box com clickable era focado pelo TalkBack sem
            // nenhum label legível, gerando "Item label — may not have a label
            // readable by screen readers". clearAndSetSemantics define uma
            // descrição única por chip, incluindo o estado de seleção:
            // "Sala de Estar, selecionado" ou "Cozinha, toque para selecionar".
            .clearAndSetSemantics {
                contentDescription = if (isSelected)
                    "${room.name}, selecionado"
                else
                    "${room.name}, toque para selecionar"
            }
    ) {
        Text(room.name, color = Color.White, fontSize = 13.sp)
    }
}