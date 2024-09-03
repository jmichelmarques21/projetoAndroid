package com.example.jornadadaconquista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jornadadaconquista.ui.theme.JornadaDaConquistaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JornadaDaConquistaTheme {
                JornadaDaConquista()
            }
        }
    }
}

private fun ColumnScope.Button(onClick: @Composable () -> Unit) {

}

fun Button(onClick: () -> Unit, function: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun JornadaDaConquista(){
    var cliques by remember {mutableStateOf(0) }
    var estadoJogo by remember { mutableStateOf(JogoEstado.INICIAL) }
    val N = remember { Random.nextInt(1,51) } // gera um número aleatório entre 1 e 50
    val imagem = when {
        cliques == N ->R.drawable.conquista
        cliques >= (N * 0.66).toInt() -> R.drawable.imgfinal
        cliques >= (N * 0.33).toInt() -> R.drawable.mediana
        else -> R.drawable.inicial
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = imagem), contentDescription = null, modifier = Modifier
            .size(450.dp)
            .clickable {
                if (cliques < N) {
                    cliques++
                    if (cliques == N) {
                        estadoJogo = JogoEstado.CONQUISTA
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            estadoJogo = JogoEstado.DESISTENCIA
        }) {
            Text("Desistir")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(estadoJogo == JogoEstado.CONQUISTA) {
            Text("Parabéns! Você alcançou a conquista!")
            Button(onClick =  {reiniciarJogo()}) {
                Text("Novo Jogo")
            }
        } else if(estadoJogo == JogoEstado.DESISTENCIA) {
            Image(
                painter = painterResource(id = R.drawable.desistencia),
                contentDescription = null,
                modifier = Modifier.size(500.dp)
            )
            Text("Você desistiu. Deseja tentar novamente? ")
            Row {
                Button(onClick = { reiniciarJogo() }) {
                    Text("Sim")
                }
                this@Column.Button(onClick = {
                    Text("Não")
                }
                )
            }
        }
    }

}

fun reiniciarJogo() {
    var cliques = 0;
    var estadoJogo = JogoEstado.INICIAL
}


enum class JogoEstado {
    INICIAL, MEDIANA, FINAL, CONQUISTA, DESISTENCIA
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    JornadaDaConquistaTheme {
        JornadaDaConquista()
    }
}