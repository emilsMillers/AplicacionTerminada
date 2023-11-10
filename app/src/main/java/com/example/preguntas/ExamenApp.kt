package com.example.preguntas

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun ExamenApp(navController: NavHostController) {
    val context = LocalContext.current
    val questions = mutableListOf(
        Question("¿Es el cielo azul?", true, R.drawable.image1),
        Question("¿2 + 2 = 5?", false, R.drawable.image2),
        Question("¿La Tierra es plana?", false, R.drawable.image3),
        Question("¿El agua hierve a 100°C?", true, R.drawable.image4),
        Question("¿La luna es un planeta?", false, R.drawable.image5)
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var showMessage by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var aciertos by remember { mutableStateOf(0) }
    var fallos by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    fun onAnswerSelected(isCorrect: Boolean) {
        if (!isGameOver) {
            selectedAnswer = true
            isAnswerCorrect = isCorrect
            showMessage = true

            if (isCorrect) {
                aciertos++
                questions[currentQuestionIndex] = questions[currentQuestionIndex].copy(responded = true, correct = true)
            } else {
                fallos++
                questions[currentQuestionIndex] = questions[currentQuestionIndex].copy(responded = true, correct = false)
            }

            isGameOver = currentQuestionIndex == questions.size - 1
        }
    }





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val currentQuestion = questions[currentQuestionIndex]

        Text(
            text = currentQuestion.text,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(id = currentQuestion.imageResource),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        AnswerButton("TRUE", selectedAnswer, questions[currentQuestionIndex].correct) {
            onAnswerSelected(isCorrect = true)
            questions[currentQuestionIndex].responded = true
        }
        Spacer(modifier = Modifier.height(16.dp))

        AnswerButton("FALSE", selectedAnswer, !questions[currentQuestionIndex].correct) {
            onAnswerSelected(isCorrect = false)
            questions[currentQuestionIndex].responded = true
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (!isGameOver) {
                        showMessage = false
                        currentQuestionIndex = (currentQuestionIndex - 1 + questions.size) % questions.size
                        selectedAnswer = false
                    }
                },
                modifier = Modifier.padding(16.dp),
                enabled = !questions[currentQuestionIndex].responded
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text("PREV")
            }

            Button(
                onClick = {
                    if (!isGameOver) {
                        showMessage = false
                        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                        selectedAnswer = false
                    }
                },
                modifier = Modifier.padding(16.dp),
                enabled = !questions[currentQuestionIndex].responded
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(stringResource(id = R.string.next_button_label))
            }
        }

        if (isGameOver) {
            AlertDialog(
                onDismissRequest = { /* Nothing to do here */ },
                title = {
                    Text("¡Juego Terminado!")
                },
                text = {
                    val totalQuestions = questions.size
                    val percentage = (fallos.toFloat() / totalQuestions) * 100

                    when {
                        percentage.toInt() == 100 -> {
                            Image(
                                painter = painterResource(id = R.drawable.imagenbueno),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                            Text("¡Excelente! Has acertado el 100% de las preguntas.")

                        }
                        percentage >= 50.0 -> {
                            Image(
                                painter = painterResource(id = R.drawable.imagenmaomeno),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                            Text("Has acertado el $percentage% de las preguntas. ¡Buen trabajo!")

                        }
                        else -> {
                            Image(
                                painter = painterResource(id = R.drawable.imagenmal),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            Text("Has suspendido. ¡Mejora la próxima vez!")

                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            navController.navigate("MainMenu")
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Menú Principal")
                    }
                }
            )
        }
    }
}


