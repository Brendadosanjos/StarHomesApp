package com.starhomes.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen

@Composable
fun NeighborhoodDetailsScreen(
    neighborhoodId: String?,
    navigateTo: (Screen) -> Unit,
    selectProperty: (String) -> Unit
) {
    val neighborhood = MockData.NEIGHBORHOODS.find { it.id == neighborhoodId }
        ?: return Text("Bairro não encontrado.", color = Color.White)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(neighborhood.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp))

        // Property grid
        val gridHeight = if (neighborhood.properties.size <= 3) 160.dp else 320.dp
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(neighborhood.properties) { property ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            selectProperty(property.id)
                            navigateTo(Screen.PROPERTY_DETAILS)
                        }
                ) {
                    AsyncImage(
                        model = property.image,
                        contentDescription = property.type,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(4.dp)
                    ) {
                        Text(property.type, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("£${property.price}/mês", color = Color.White, fontSize = 9.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Simple map placeholder (clickable to open Google Maps)
        val mapsUrl = "https://www.google.com/maps/search/?api=1&query=${neighborhood.center.lat},${neighborhood.center.lng}"
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "📍 ${neighborhood.name}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Lat: ${neighborhood.center.lat}, Lng: ${neighborhood.center.lng}",
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )
                neighborhood.properties.forEach { p ->
                    if (p.lat != null && p.lng != null) {
                        Text("• ${p.type} — £${p.price}", color = Color(0xFF9CA3AF), fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                TextButton(
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl)))
                    }
                ) {
                    Text("Abrir com o Google Maps", color = Color(0xFF60A5FA))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
