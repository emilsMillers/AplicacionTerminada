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


data class Statistics(var aciertos: Int = 0, var fallos: Int = 0, var total: Int = 0)

object StatisticsManager {
    private const val PREFERENCES_NAME = "preguntas_preferences"
    fun updateStatistics(context: Context, isAnswerCorrect: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        val aciertos = sharedPref.getInt("Aciertos", 0)
        val fallos = sharedPref.getInt("Fallos", 0)
        val total = sharedPref.getInt("Total", 0)

        sharedPref.edit()
            .putInt("Aciertos", aciertos + if (isAnswerCorrect) 1 else 0)
            .putInt("Fallos", fallos + if (!isAnswerCorrect) 1 else 0)
            .putInt("Total", total + 1)
            .apply()
    }

    fun getStatistics(context: Context): Statistics {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        return Statistics(
            aciertos = sharedPref.getInt("Aciertos", 0),
            fallos = sharedPref.getInt("Fallos", 0),
            total = sharedPref.getInt("Total", 0)
        )
    }
}


@Composable
fun QuestionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val questions = listOf(
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
            selectedAnswer = true
            isAnswerCorrect = true
            showMessage = true
            if (questions[currentQuestionIndex].correct) {
                aciertos++
                StatisticsManager.updateStatistics(context, true)
            } else {
                fallos++
                StatisticsManager.updateStatistics(context, false)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnswerButton("FALSE", selectedAnswer, !questions[currentQuestionIndex].correct) {
            selectedAnswer = true
            isAnswerCorrect = false
            showMessage = true
            if (!questions[currentQuestionIndex].correct) {
                aciertos++
                StatisticsManager.updateStatistics(context, true)
            } else {
                fallos++
                StatisticsManager.updateStatistics(context, false)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (showMessage) {
            val message = if (isAnswerCorrect) stringResource(id = R.string.correct_message) else stringResource(id = R.string.incorrect_message)
            val messageColor = if (isAnswerCorrect) Color.Green else Color.Red

            Text(
                text = message,
                color = messageColor,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    showMessage = false
                    currentQuestionIndex = (currentQuestionIndex - 1 + questions.size) % questions.size
                    selectedAnswer = false
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text("PREV")
            }

            Button(
                onClick = {
                    showMessage = false
                    currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                    selectedAnswer = false
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(stringResource(id = R.string.next_button_label))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    showMessage = false
                    currentQuestionIndex = (0 until questions.size).random()
                    selectedAnswer = false
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("RANDOM")
            }
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

            Button(
                onClick = {
                    navController.navigate("statistics")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Estadísticas")
            }
        }
    }
}




data class Question(
    val text: String,
    val correct: Boolean,
    val imageResource: Int,
    var responded: Boolean = false
)



@Composable
fun AnswerButton(
    text: String,
    selected: Boolean,
    isCorrect: Boolean,
    onAnswerSelected: () -> Unit
) {
    val buttonColor = if (selected && isCorrect) {
        Color.Green
    } else if (selected && !isCorrect) {
        Color.Red
    } else {
        MaterialTheme.colorScheme.primary
    }

    Button(
        onClick = {
            onAnswerSelected()
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(buttonColor)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black
        )
    }
}




