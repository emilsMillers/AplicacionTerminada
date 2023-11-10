package com.example.preguntas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun StatisticsScreen(navController: NavHostController) {
    val statistics = StatisticsManager.getStatistics(context = LocalContext.current)

    val porcentajeAciertos = if (statistics.total > 0) {
        (statistics.aciertos.toFloat() / statistics.total.toFloat() * 100).toInt()
    } else {
        0
    }

    val porcentajeFallos = if (statistics.total > 0) {
        (statistics.fallos.toFloat() / statistics.total.toFloat() * 100).toInt()
    } else {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Estadísticas", style = MaterialTheme.typography.titleMedium)

        Text("Aciertos: ${statistics.aciertos}")
        Text("Fallos: ${statistics.fallos}")
        Text("Total: ${statistics.total}")

        Text("Porcentaje de aciertos: $porcentajeAciertos%")
        Text("Porcentaje de fallos: $porcentajeFallos%")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                navController.navigate("MainMenu")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Menú Principal")
        }
    }
}

