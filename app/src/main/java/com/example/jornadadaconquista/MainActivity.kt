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
                onExitApp = { finish() }
            )
        }
    }
}

@Composable
fun GameScreen(onExitApp: () -> Unit) {
    // Define o número de cliques necessários para alcançar a conquista (valor aleatório entre 1 e 50)
    var targetClicks by remember { mutableIntStateOf(Random.nextInt(1, 51)) }

    // Contador de cliques do usuário
    var clickCount by remember { mutableIntStateOf(0) }

    // Estado do jogo, exibindo mensagens ao usuário
    var gameStatus by remember { mutableStateOf("Comece sua jornada!") }

    // Indica se o jogo foi concluído
    var gameOver by remember { mutableStateOf(false) }

    // Indica se o jogador desistiu
    var hasGivenUp by remember { mutableStateOf(false) }

    // Efeito de clique na imagem
    var imageClicked by remember { mutableStateOf(false) }

    // Estado para controlar a exibição da imagem correspondente
    val imageResId = when {
        gameOver -> R.drawable.conquista // Imagem de conquista
        hasGivenUp -> R.drawable.desistencia // Imagem de desistência
        clickCount >= (targetClicks * 0.66).toInt() -> R.drawable.imgfinal // Imagem final
        clickCount >= (targetClicks * 0.33).toInt() -> R.drawable.mediana // Imagem mediana
        else -> R.drawable.inicial // Imagem inicial
    }

    // Animações
    val scale by animateFloatAsState(if (imageClicked) 0.9f else 1f)
    val rotation by animateFloatAsState(if (imageClicked) 10f else 0f)
    val alpha by animateFloatAsState(if (imageClicked) 0.7f else 1f)

    // Interface do jogo
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50.dp)) // Bordas arredondadas
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    alpha = alpha
                )
                .clickable {
                    // Incrementa o número de cliques e verifica se o jogador alcançou a conquista
                    if (!gameOver && !hasGivenUp) {
                        clickCount++
                        imageClicked = true
                        if (clickCount >= targetClicks) {
                            gameStatus = "Você alcançou sua conquista!"
                            gameOver = true
                        }
                    }
                }
        )

        // Reseta o estado de clique da imagem após um pequeno intervalo
        LaunchedEffect(imageClicked) {
            if (imageClicked) {
                kotlinx.coroutines.delay(150) // atraso de 150 ms para o efeito de clique
                imageClicked = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            gameOver -> {
                Text(text = "Parabéns! Você alcançou a conquista!")
                Button(onClick = {
                    restartGame(
                        resetClickCount = { clickCount = 0 },
                        resetGameStatus = { gameStatus = "Comece sua jornada!" },
                        resetGameOver = { gameOver = false },
                        resetHasGivenUp = { hasGivenUp = false },
                        resetTargetClicks = { targetClicks = Random.nextInt(1, 51) }
                    )
                }) {
                    Text("Novo Jogo")
                }
            }
            hasGivenUp -> {
                Text(text = "Você desistiu. Deseja tentar novamente?")
                Row {
                    Button(onClick = {
                        restartGame(
                            resetClickCount = { clickCount = 0 },
                            resetGameStatus = { gameStatus = "Comece sua jornada!" },
                            resetGameOver = { gameOver = false },
                            resetHasGivenUp = { hasGivenUp = false },
                            resetTargetClicks = { targetClicks = Random.nextInt(1, 51) }
                        )
                    }) {
                        Text("Sim")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onExitApp) {
                        Text("Não")
                    }
                }
            }
            else -> {
                Text(text = "Cliques: $clickCount / $targetClicks")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    hasGivenUp = true
                    gameStatus = "Você desistiu."
                }) {
                    Text("Desistir")
                }
            }
        }
    }
}

// Função para reiniciar o jogo
fun restartGame(
    resetClickCount: () -> Unit,
    resetGameStatus: () -> Unit,
    resetGameOver: () -> Unit,
    resetHasGivenUp: () -> Unit,
    resetTargetClicks: () -> Unit
) {
    // Redefine todas as variáveis do jogo para o estado inicial
    resetClickCount()
    resetGameStatus()
    resetGameOver()
    resetHasGivenUp()
    resetTargetClicks()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameScreen(onExitApp = {})
}
