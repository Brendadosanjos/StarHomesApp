package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun SearchResultsScreen(
    navigateTo: (Screen) -> Unit,
    selectNeighborhood: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Resultados da Busca",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp)
        )
        Text(
            "Aqui estão algumas opções de bairros que podem ser adequados para você.",
            color = Gray400,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(MockData.NEIGHBORHOODS) { neighborhood ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Gray800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = neighborhood.image,
                            contentDescription = neighborhood.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(neighborhood.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text(neighborhood.description, color = Gray400, fontSize = 12.sp)
                            Text(neighborhood.priceRange, color = Blue400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                selectNeighborhood(neighborhood.id)
                                navigateTo(Screen.NEIGHBORHOOD_DETAILS)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("Ver", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
