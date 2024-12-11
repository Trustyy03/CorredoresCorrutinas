package com.example.corredorescorrutinas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarreraApp()
        }
    }
}
@Composable
fun CarreraApp() {
    var corredor1Progreso by remember { mutableStateOf(0f) }
    var corredor2Progreso by remember { mutableStateOf(0f) }
    var corriendo by remember { mutableStateOf(false) }
// Scope para las corrutinas
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simulación de Corrutinas: Carrera de Corredores", style =
        MaterialTheme.typography.titleLarge)
        CarreraCanvas(corredor1Progreso, corredor2Progreso)
        Button(
            onClick = {
                if (!corriendo) {
                    corriendo = true
// Corrutinas para cada corredor
                    scope.launch {
                        correr(corredor1Progreso, { corredor1Progreso = it },
                            100)
                        corriendo = false
                    }
                    scope.launch {
                        correr(corredor2Progreso, { corredor2Progreso = it },
                            200)
                    }
                }
            },
            enabled = !corriendo
        ) {
            Text("¡Iniciar Carrera!")
        }
        Button(
            onClick = {
                scope.coroutineContext.cancelChildren() // Detener todas las corrutinas activas
                corredor1Progreso = 0f
                corredor2Progreso = 0f
                corriendo = false
            }
        ) {
            Text("Reiniciar")
        }
    }
}
@Composable
fun CarreraCanvas(corredor1: Float, corredor2: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
    ) {
        val anchoPista = size.width
        val altura = size.height
// Dibujar pista
        drawLine(Color.Black, start = Offset(0f, altura / 3), end =
        Offset(anchoPista, altura / 3), strokeWidth = 4f)
        drawLine(Color.Black, start = Offset(0f, 2 * altura / 3), end =
        Offset(anchoPista, 2 * altura / 3), strokeWidth = 4f)
// Dibujar corredores
        val corredor1PosX = corredor1 * anchoPista
        val corredor2PosX = corredor2 * anchoPista
        val radioCorredor = 20.dp.toPx()
        drawCircle(Color.Blue, radius = radioCorredor, center =
        Offset(corredor1PosX, altura / 3))
        drawCircle(Color.Red, radius = radioCorredor, center =
        Offset(corredor2PosX, 2 * altura / 3))
    }
}
suspend fun correr(progresoInicial: Float, actualizarProgreso: (Float) -> Unit,
                   delayMs: Long) {
    var progreso = progresoInicial
    while (progreso < 1f) {
        progreso += 0.01f
        actualizarProgreso(progreso)
        delay(delayMs)
    }
}