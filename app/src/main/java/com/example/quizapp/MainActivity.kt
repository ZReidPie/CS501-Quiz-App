package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    // flashcard and questions
    val questions = listOf(
        "Who painted the Mona Lisa?" to "Leonardo da Vinci",
        "In the story of Snow White, how many dwarfs are there?" to "Seven",
        "What do bees collect to make honey?" to "Nectar",
        "Who is the Greek god of war and son of Zeus and Hera?" to "Ares",
        "What is the capital of France?" to "Paris",
        "What is the largest planet in our solar system?" to "Jupiter",
        "What is 2 + 2?" to "4"
    )

    // State to track current question index and user input
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }

    // For Snackbars
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // <-- Fix layout issues
        horizontalAlignment = Alignment.CenterHorizontally // <-- Fix layout issues
    ) {
        if (quizComplete) {
            // Show Quiz Complete message
            Text(text = "Quiz Complete!", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Reset the quiz
                currentQuestionIndex = 0
                quizComplete = false
            }) {
                Text(text = "Restart Quiz")
            }
        } else {
            // Display the current flashcard question
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = questions[currentQuestionIndex].first,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TextField for user input
            TextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Your Answer") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Answer Button
            Button(onClick = {
                // Check the answer
                val correctAnswer = questions[currentQuestionIndex].second
                if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Correct!")
                    }
                    // Move to the next question
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                        userAnswer = "" // Reset answer field
                        // quiz is complete case
                    } else {
                        quizComplete = true
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Incorrect! The answer is $correctAnswer")
                    }
                }
            }) {
                Text(text = "Submit Answer")
            }
        }

        // Snackbar for feedback
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    QuizAppTheme {
        QuizApp()
    }
}
