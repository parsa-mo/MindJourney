package com.example.mindjourney.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.data.Thought
import com.example.mindjourney.data.ThoughtDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ThoughtsScreen(navController: NavController) {
    val context = LocalContext.current
    val thoughtDao = ThoughtDatabase.getDatabase(context).thoughtDao()
    val thoughts by thoughtDao.getThoughtsForUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        .observeAsState(initial = emptyList())

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Thought Diaries",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )

                Divider(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                    thickness = 3.dp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(thoughts) { thought ->
                        ExpandableThoughtFormCard(
                            thought = thought,
                            onDelete = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    thoughtDao.delete(thought)
                                }
                            },
                            onSave = { updatedThought ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    thoughtDao.update(updatedThought)
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ExpandableThoughtFormCard(thought: Thought, onDelete: () -> Unit, onSave: (Thought) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(TextFieldValue(thought.title)) }
    var prompt1 by remember { mutableStateOf(TextFieldValue(thought.prompt1)) }
    var prompt2 by remember { mutableStateOf(TextFieldValue(thought.prompt2)) }
    var prompt3 by remember { mutableStateOf(TextFieldValue(thought.prompt3)) }
    var beliefRating by remember { mutableStateOf(thought.beliefRating.toFloat()) }
    var consequenceRating by remember { mutableStateOf(thought.consequenceRating.toFloat()) }
    val date by remember { mutableStateOf(thought.date) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Delete Record") },
            text = { Text(text = "Are you sure you want to delete this record?") }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.text,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = date, style = MaterialTheme.typography.bodyMedium)
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Gray)
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // Editable Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Editable Prompt 1
                Text(
                    text = "Prompt 1: An actual event or situation, a thought, mental picture, or physical trigger.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt1,
                    onValueChange = { prompt1 = it },
                    label = { Text("Activating Event") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Editable Prompt 2 with Rating Slider
                Text(
                    text = "Prompt 2: List all self-statements that link A to C. Ask yourself: 'What was I thinking?' Rate how much you believe this thought between 1 to 10.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt2,
                    onValueChange = { prompt2 = it },
                    label = { Text("Beliefs") },
                    modifier = Modifier.fillMaxWidth()
                )
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

                // Editable Prompt 3 with Rating Slider
                Text(
                    text = "Prompt 3: Write down words describing how you feel. Rate the intensity of this feeling between 1 to 10.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = prompt3,
                    onValueChange = { prompt3 = it },
                    label = { Text("Consequences") },
                    modifier = Modifier.fillMaxWidth()
                )
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

                // Save Button for Updated Thought
                Button(onClick = {
                    val updatedThought = thought.copy(
                        title = title.text,
                        prompt1 = prompt1.text,
                        prompt2 = prompt2.text,
                        prompt3 = prompt3.text,
                        beliefRating = beliefRating.toInt(),
                        consequenceRating = consequenceRating.toInt(),
                        date = date
                    )
                    onSave(updatedThought)
                    isExpanded = false // Close card after saving
                }) {
                    Text("Save Changes")
                }
            }
        }
    }
}
