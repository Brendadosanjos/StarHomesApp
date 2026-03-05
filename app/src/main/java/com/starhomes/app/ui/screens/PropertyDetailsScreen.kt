package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800
import com.starhomes.app.ui.components.PrimaryButton
import com.starhomes.app.ui.components.SecondaryButton

@Composable
fun PropertyDetailsScreen(
    propertyId: String?,
    isFavorite: Boolean,
    onToggleFavorite: (String) -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val property = MockData.findProperty(propertyId ?: "")
        ?: return Text("Imóvel não encontrado.", color = Color.White)
    val neighborhood = MockData.findNeighborhoodByProperty(property.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = property.image,
            contentDescription = property.type,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(12.dp))

        Text(property.type, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("em ${neighborhood?.name}", color = Gray400, fontSize = 14.sp)
        Text("£${property.price}/mês", color = Blue400, fontSize = 22.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))

        // Info grid
        Card(
            colors = CardDefaults.cardColors(containerColor = Gray800),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoCell("Quartos", "${property.bedrooms}")
                InfoCell("Área", "70 m²")
                InfoCell("Aceita pets", "Sim")
                InfoCell("Próx. metrô", "300 m")
            }
        }
        Spacer(Modifier.height(20.dp))

        PrimaryButton("Agendar visita", onClick = { navigateTo(Screen.SCHEDULE_VISIT) })
        Spacer(Modifier.height(12.dp))
        SecondaryButton("Fazer tour virtual", onClick = { navigateTo(Screen.VIRTUAL_TOUR) })
        Spacer(Modifier.height(12.dp))

        // Favorite button
        Button(
            onClick = { onToggleFavorite(property.id) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color(0xFF7F1D1D).copy(alpha = 0.7f) else Color(0xFF374151)
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color(0xFFF87171) else Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (isFavorite) "Remover favorito" else "Salvar favorito",
                color = if (isFavorite) Color(0xFFF87171) else Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InfoCell(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color(0xFF9CA3AF), fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
