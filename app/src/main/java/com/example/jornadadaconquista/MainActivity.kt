package com.example.jornadadaconquista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen(
                onSairApp = { finish() }
            )
        }
    }
}

@Composable
fun GameScreen(onSairApp: () -> Unit) {
    // Define o número de cliques necessários para alcançar a conquista (valor aleatório entre 1 e 50)
    var alvoCliques by remember { mutableIntStateOf(Random.nextInt(1, 51)) }

    // Contador de cliques do usuário
    var contaCliques by remember { mutableIntStateOf(0) }

    // Estado do jogo, exibindo mensagens ao usuário
    var statusJogo by remember { mutableStateOf("Comece sua jornada!") }

    // Indica se o jogo foi concluído
    var jogoAcabou by remember { mutableStateOf(false) }

    // Indica se o jogador desistiu
    var desistiu by remember { mutableStateOf(false) }

    // Efeito de clique na imagem
    var imagemClicada by remember { mutableStateOf(false) }

    // Estado para controlar a exibição da imagem correspondente
    val imagemId = when {
        jogoAcabou -> R.drawable.conquista // Imagem de conquista
        desistiu -> R.drawable.desistencia // Imagem de desistência
        contaCliques >= (alvoCliques * 0.66).toInt() -> R.drawable.imgfinal // Imagem final
        contaCliques >= (alvoCliques * 0.33).toInt() -> R.drawable.mediana // Imagem mediana
        else -> R.drawable.inicial // Imagem inicial
    }

    // Animações
    val escala by animateFloatAsState(if (imagemClicada) 0.9f else 1f)
    val rotacao by animateFloatAsState(if (imagemClicada) 10f else 0f)
    val transparencia by animateFloatAsState(if (imagemClicada) 0.7f else 1f)

    // Interface do jogo
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imagemId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50.dp)) // Bordas arredondadas
                .graphicsLayer(
                    scaleX = escala,
                    scaleY = escala,
                    rotationZ = rotacao,
                    alpha = transparencia
                )
                .clickable {
                    // Incrementa o número de cliques e verifica se o jogador alcançou a conquista
                    if (!jogoAcabou && !desistiu) {
                        contaCliques++
                        imagemClicada = true
                        if (contaCliques >= alvoCliques) {
                            statusJogo = "Você alcançou sua conquista!"
                            jogoAcabou = true
                        }
                    }
                }
        )

        // Reseta o estado de clique da imagem após um pequeno intervalo
        LaunchedEffect(imagemClicada) {
            if (imagemClicada) {
                kotlinx.coroutines.delay(150) // atraso de 150 ms para o efeito de clique
                imagemClicada = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            jogoAcabou -> {
                Text(text = "Parabéns! Você alcançou a conquista!")
                Button(onClick = {
                    reiniciarJogo(
                        resetContaCliques = { contaCliques = 0 },
                        resetStatusJogo = { statusJogo = "Comece sua jornada!" },
                        resetJogoAcabou = { jogoAcabou = false },
                        resetDesistiu = { desistiu = false },
                        resetAlvoCliques = { alvoCliques = Random.nextInt(1, 51) }
                    )
                }) {
                    Text("Novo Jogo")
                }
            }
            desistiu -> {
                Text(text = "Você desistiu. Deseja tentar novamente?")
                Row {
                    Button(onClick = {
                        reiniciarJogo(
                            resetContaCliques = { contaCliques = 0 },
                            resetStatusJogo = { statusJogo = "Comece sua jornada!" },
                            resetJogoAcabou = { jogoAcabou = false },
                            resetDesistiu = { desistiu = false },
                            resetAlvoCliques = { alvoCliques = Random.nextInt(1, 51) }
                        )
                    }) {
                        Text("Sim")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onSairApp) {
                        Text("Não")
                    }
                }
            }
            else -> {
                Text(text = "Cliques: $contaCliques / $alvoCliques")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    desistiu = true
                    statusJogo = "Você desistiu."
                }) {
                    Text("Desistir")
                }
            }
        }
    }
}

// Função para reiniciar o jogo
fun reiniciarJogo(
    resetContaCliques: () -> Unit,
    resetStatusJogo: () -> Unit,
    resetJogoAcabou: () -> Unit,
    resetDesistiu: () -> Unit,
    resetAlvoCliques: () -> Unit
) {
    // Redefine todas as variáveis do jogo para o estado inicial
    resetContaCliques()
    resetStatusJogo()
    resetJogoAcabou()
    resetDesistiu()
    resetAlvoCliques()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameScreen(onSairApp = {})
}