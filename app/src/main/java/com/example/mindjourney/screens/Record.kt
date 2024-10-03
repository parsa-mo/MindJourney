package com.example.mindjourney.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.data.Thought
import com.example.mindjourney.data.ThoughtDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecordScreen(navController: NavController) {
    // Database DAO
    val context = LocalContext.current
    val thoughtDao = ThoughtDatabase.getDatabase(context).thoughtDao()

    // Reset form states every time we come to this screen
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var prompt1 by remember { mutableStateOf(TextFieldValue("")) }
    var prompt2 by remember { mutableStateOf(TextFieldValue("")) }
    var prompt3 by remember { mutableStateOf(TextFieldValue("")) }
    var beliefRating by remember { mutableStateOf(5f) }
    var consequenceRating by remember { mutableStateOf(5f) }
    var date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }

    // Date Picker Dialog Setup
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val focusRequesterTitle = remember { FocusRequester() }
    val focusRequesterPrompt1 = remember { FocusRequester() }
    val focusRequesterPrompt2 = remember { FocusRequester() }
    val focusRequesterPrompt3 = remember { FocusRequester() }

    var showError by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()) // Enables scrolling
            ) {
                Text(
                    text = "Please fill all fields to save your record",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (showError) Color.Red else Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    isError = showError && title.text.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterTitle)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date Picker
                Button(
                    onClick = {
                        datePickerDialog.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Date: $date")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Activating Event (Prompt 1)
                Text(
                    text = "Prompt 1: An actual event or situation, a thought, mental picture, or physical trigger.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt1,
                    onValueChange = { prompt1 = it },
                    label = { Text("Activating Event") },
                    isError = showError && prompt1.text.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPrompt1)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Beliefs (Prompt 2)
                Text(
                    text = "Prompt 2: List all self-statements that link A to C. Ask yourself: 'What was I thinking?' Rate how much you believe this thought between 1 to 10.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt2,
                    onValueChange = { prompt2 = it },
                    label = { Text("Beliefs") },
                    isError = showError && prompt2.text.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPrompt2)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Belief Rating")
                Slider(
                    value = beliefRating,
                    onValueChange = { beliefRating = it },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Rating: ${beliefRating.toInt()}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))

                // Consequences (Prompt 3)
                Text(
                    text = "Prompt 3: Write down words describing how you feel. Rate the intensity of this feeling between 1 to 10.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt3,
                    onValueChange = { prompt3 = it },
                    label = { Text("Consequences") },
                    isError = showError && prompt3.text.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPrompt3)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Consequence Rating")
                Slider(
                    value = consequenceRating,
                    onValueChange = { consequenceRating = it },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Rating: ${consequenceRating.toInt()}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(24.dp))

                // Save and Cancel Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        if (title.text.isBlank() || prompt1.text.isBlank() || prompt2.text.isBlank() || prompt3.text.isBlank()) {
                            showError = true
                            when {
                                title.text.isBlank() -> focusRequesterTitle.requestFocus()
                                prompt1.text.isBlank() -> focusRequesterPrompt1.requestFocus()
                                prompt2.text.isBlank() -> focusRequesterPrompt2.requestFocus()
                                prompt3.text.isBlank() -> focusRequesterPrompt3.requestFocus()
                            }
                        } else {
                            // Save the new thought
                            val newThought = Thought(
                                id = 0, // For a new thought, the ID should be auto-generated
                                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                title = title.text,
                                prompt1 = prompt1.text,
                                prompt2 = prompt2.text,
                                prompt3 = prompt3.text,
                                beliefRating = beliefRating.toInt(),
                                consequenceRating = consequenceRating.toInt(),
                                date = date // Using formatted string date
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                thoughtDao.insert(newThought)
                            }
                            navController.navigate("thoughts")
                        }
                    }) {
                        Text("Save")
                    }

                    Button(onClick = {
                        navController.navigate("dashboard")
                    }) {
                        Text("Cancel")
                    }
                }
            }
        }
    )
}
