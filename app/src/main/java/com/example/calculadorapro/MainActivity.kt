package com.example.calculadorapro

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadorapro.ui.theme.CalculadoraProTheme

data class BotonModelo(
    val id: String,
    var numero: String,
    var operacion_aritmetica: OperacionesAritmeticas = OperacionesAritmeticas.Ninguna,
    var operacion_a_mostrar: String = ""
)

enum class EstadosCalculadora{
    CuandoEstaEnCero,
    AgregandoNumeros,
    SeleccionadoOperacion,
    MostrandoResultado
}

enum class OperacionesAritmeticas{
    Ninguna,
    Suma,
    Resta,
    Multiplicacion,
    Division,
    Resultado
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraProTheme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {
    var pantalla_calculadora by remember { mutableStateOf("0") }
    var numero_anterior by remember { mutableStateOf("0") }
    var estado_de_la_calculadora by remember { mutableStateOf(EstadosCalculadora.CuandoEstaEnCero) }
    var operacion_seleccionada by remember { mutableStateOf(OperacionesAritmeticas.Ninguna) }
    var nuevo_numero by remember { mutableStateOf(false) }

    fun realizarOperacion(): String {
        return when (operacion_seleccionada) {
            OperacionesAritmeticas.Suma -> (numero_anterior.toDouble() + pantalla_calculadora.toDouble()).toString()
            OperacionesAritmeticas.Resta -> (numero_anterior.toDouble() - pantalla_calculadora.toDouble()).toString()
            OperacionesAritmeticas.Multiplicacion -> (numero_anterior.toDouble() * pantalla_calculadora.toDouble()).toString()
            OperacionesAritmeticas.Division -> if (pantalla_calculadora.toDouble() != 0.0) (numero_anterior.toDouble() / pantalla_calculadora.toDouble()).toString() else "Error"
            else -> pantalla_calculadora
        }
    }

    fun pulsar_boton(boton: BotonModelo) {
        when {
            boton.operacion_aritmetica != OperacionesAritmeticas.Ninguna && boton.operacion_aritmetica != OperacionesAritmeticas.Resultado -> {
                numero_anterior = pantalla_calculadora
                operacion_seleccionada = boton.operacion_aritmetica
                nuevo_numero = true
            }

            boton.operacion_aritmetica == OperacionesAritmeticas.Resultado -> {
                pantalla_calculadora = realizarOperacion()
                estado_de_la_calculadora = EstadosCalculadora.MostrandoResultado
                nuevo_numero = true
            }

            else -> {
                if (nuevo_numero) {
                    pantalla_calculadora = boton.numero
                    nuevo_numero = false
                } else {
                    pantalla_calculadora =
                        if (pantalla_calculadora == "0") boton.numero else pantalla_calculadora + boton.numero
                }
            }
        }
    }
    Box {
        Image(painter = painterResource(R.drawable.walapaper),
            contentDescription = "Una foto hard",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pantalla_calculadora,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .height(50.dp),
                textAlign = TextAlign.Right,
                color = Color.White,
                fontSize = 56.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Boton(
                    "+",
                    { pulsar_boton(BotonModelo("boton_suma", "+", OperacionesAritmeticas.Suma)) })
                Boton(
                    "-",
                    { pulsar_boton(BotonModelo("boton_resta", "-", OperacionesAritmeticas.Resta)) })
                Boton(
                    "×",
                    {
                        pulsar_boton(
                            BotonModelo(
                                "boton_multiplicacion",
                                "×",
                                OperacionesAritmeticas.Multiplicacion
                            )
                        )
                    })
                Boton(
                    "÷",
                    {
                        pulsar_boton(
                            BotonModelo(
                                "boton_division",
                                "÷",
                                OperacionesAritmeticas.Division
                            )
                        )
                    })
            }

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("7", "8", "9").forEach { numero ->
                        Boton(numero) { pulsar_boton(BotonModelo("boton_$numero", numero)) }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("4", "5", "6").forEach { numero ->
                        Boton(numero) { pulsar_boton(BotonModelo("boton_$numero", numero)) }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("1", "2", "3").forEach { numero ->
                        Boton(numero) { pulsar_boton(BotonModelo("boton_$numero", numero)) }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Boton("0") { pulsar_boton(BotonModelo("boton_0", "0")) }
                    Boton("=") {
                        pulsar_boton(
                            BotonModelo(
                                "boton_resultado",
                                "=",
                                OperacionesAritmeticas.Resultado
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Boton(etiqueta: String, alPulsar: () -> Unit = {}) {
    Button(onClick = alPulsar, modifier = Modifier.size(80.dp)) {
        Text(etiqueta, textAlign = TextAlign.Center, fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculadora() {
    CalculadoraProTheme {
        Calculadora()
    }
}
