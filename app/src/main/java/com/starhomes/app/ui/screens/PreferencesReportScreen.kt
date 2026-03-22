package com.starhomes.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.ui.Blue400
import com.starhomes.app.ui.Gray400
import com.starhomes.app.ui.Gray800

data class ChartItem(val name: String, val value: Float, val color: Color)

@Composable
fun PreferencesReportScreen(navigateTo: (Screen) -> Unit) {
    val prefs = MockData.PREFERENCES

    val chartData = listOf(
        ChartItem("Transporte", if (prefs.priorities.transport) 40f else 10f, Color(0xFF60A5FA)),
        ChartItem("Segurança", if (prefs.priorities.safety) 35f else 10f, Color(0xFF34D399)),
        ChartItem("Escolas", if (prefs.priorities.schools) 25f else 10f, Color(0xFFFBBF24)),
    )
    val total = chartData.sumOf { it.value.toDouble() }.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Relatório de Preferências",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Com base no seu perfil e nas suas preferências, o Star Homes AI analisou o que é mais importante para você.",
            color = Gray400,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        DonutChart(data = chartData, total = total)
        Spacer(Modifier.height(16.dp))

        chartData.forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color = item.color)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${item.name}: ${((item.value / total) * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Gray800),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReportItem("Perfil", prefs.profileType.label)
                ReportItem("Faixa de preço", "£${prefs.priceRange.first} - £${prefs.priceRange.second}/mês")
                ReportItem("Transporte prioritário", if (prefs.priorities.transport) "Sim" else "Não")
                ReportItem("Segurança prioritária", if (prefs.priorities.safety) "Sim" else "Não")
                ReportItem("Escolas prioritárias", if (prefs.priorities.schools) "Sim" else "Não")
            }
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = { navigateTo(Screen.PROFILE_SETUP) }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                tint = Blue400,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(text = "Editar minhas preferências", color = Blue400)
        }
    }
}

@Composable
private fun DonutChart(data: List<ChartItem>, total: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val strokeWidth = 40f
            var startAngle = -90f
            data.forEach { item ->
                val sweep = (item.value / total) * 360f
                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
                startAngle += sweep
            }
        }
        Text(text = "Perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
private fun ReportItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Gray400, fontSize = 14.sp)
        Text(text = value, color = Blue400, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}