package com.example.corutinaloteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.corutinaloteria.ui.theme.CorutinaLoteriaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorutinaLoteriaTheme {
                PantallaLoteria()
            }
        }
    }
}

@Composable
fun PantallaLoteria() {
    ContenidoLoteria(Modifier.padding(16.dp))
}

@Composable
fun ContenidoLoteria(modifier: Modifier = Modifier) {
    var numeros by remember { mutableStateOf(generarNumerosUnicos(7, 1..70)) }
    var numeroActual by remember { mutableStateOf<Int?>(null) }
    var indiceActual by remember { mutableStateOf(0) }
    var cargando by remember { mutableStateOf(false) }

    val scopeCorutina = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Simulador de Lotería", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(26.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until 7) {
                Esfera(if (i < indiceActual) numeros[i] else null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (cargando) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                if (!cargando) {
                    cargando = true

                    numeros = generarNumerosUnicos(7, 1..70)
                    indiceActual = 0
                    numeroActual = null
                    scopeCorutina.launch {
                        for (i in numeros.indices) {
                            numeroActual = numeros[i]
                            indiceActual = i + 1
                            delay(2000)
                        }
                        cargando = false 
                    }
                }
            }, enabled = !cargando) {
                Text(text = "Generar Nuevos Números")
            }
        }
    }
}

@Composable
fun Esfera(numero: Int?) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Red, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = numero?.toString() ?: "", color = Color.White)
    }
}

fun generarNumerosUnicos(cantidad: Int, rango: IntRange): List<Int> {
    val numerosUnicos = mutableSetOf<Int>()
    while (numerosUnicos.size < cantidad) {
        numerosUnicos.add(Random.nextInt(rango.first, rango.last + 1))
    }
    return numerosUnicos.toList()
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaPantallaLoteria() {
    PantallaLoteria()
}
