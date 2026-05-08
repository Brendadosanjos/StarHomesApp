package com.starhomes.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800

@Composable
fun FavoritesScreen(
    favoriteIds: List<String>,
    navigateTo: (Screen) -> Unit,
    selectProperty: (String) -> Unit
) {
    // =========================================================================
    // OTIMIZAÇÃO 2 — remember para evitar recálculo em toda recomposição
    // -------------------------------------------------------------------------
    // ANTES:
    //   val favoriteProperties = favoriteIds.mapNotNull { MockData.findProperty(it) }
    //
    // PROBLEMA: esta linha executa uma busca linear (O(n)) para cada item da
    // lista a cada recomposição do Composable. Recomposições são frequentes —
    // qualquer mudança de estado no pai (ex.: animação do footer, mudança de
    // tema) recalculava toda a lista desnecessariamente.
    //
    // DEPOIS: remember(favoriteIds) cacheia o resultado e só recalcula quando
    // favoriteIds realmente mudar (favoritar ou desfavoritar um imóvel).
    // Em dispositivos lentos com muitos favoritos, isso elimina travadas
    // perceptíveis durante animações e navegação.
    // =========================================================================
    val favoriteProperties = remember(favoriteIds) {
        favoriteIds.mapNotNull { MockData.findProperty(it) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Meus Favoritos",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoriteProperties.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Você não possui favoritos.",
                    color = Gray400,
                    fontSize = 16.sp,
                    modifier = Modifier.semantics {
                        contentDescription = "Lista vazia. Você não possui imóveis favoritos."
                    }
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(
                    items = favoriteProperties,
                    // PERFORMANCE: key estável evita que o Compose recrie e
                    // reposicione itens quando a lista é reordenada. Sem key,
                    // remover um favorito do meio recria todos os itens abaixo.
                    key = { it.id }
                ) { property ->
                    // remember(property.id) cacheia a busca do bairro por item —
                    // evita chamar findNeighborhoodByProperty a cada recomposição.
                    val neighborhood = remember(property.id) {
                        MockData.findNeighborhoodByProperty(property.id)
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Gray800),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            selectProperty(property.id)
                            navigateTo(Screen.PROPERTY_DETAILS)
                        },
                        modifier = Modifier.semantics(mergeDescendants = true) {
                            contentDescription =
                                "${property.type} em ${neighborhood?.name ?: "bairro não informado"}, " +
                                        "£${property.price} por mês. Toque para ver detalhes."
                            role = Role.Button
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // OTIMIZAÇÃO 1 aplicada aqui também: placeholder + error
                            AsyncImage(
                                model = property.image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                placeholder = rememberVectorPainter(Icons.Default.Home),
                                error = rememberVectorPainter(Icons.Default.BrokenImage),
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(property.type, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("em ${neighborhood?.name ?: ""}", color = Gray400, fontSize = 12.sp)
                                Text(
                                    "£${property.price}/mês",
                                    color = Blue400,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}