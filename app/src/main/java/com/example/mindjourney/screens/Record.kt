import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.components.ThoughtFormViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecordScreen(navController: NavController, viewModel: ThoughtFormViewModel = viewModel()) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userUid = currentUser?.uid ?: ""

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var prompt1 by remember { mutableStateOf(TextFieldValue("")) }
    var prompt2 by remember { mutableStateOf(TextFieldValue("")) }
    var prompt3 by remember { mutableStateOf(TextFieldValue("")) }
    var beliefRating by remember { mutableStateOf(5f) }
    var consequenceRating by remember { mutableStateOf(5f) }
    val date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }

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

                Spacer(modifier = Modifier.height(8.dp))

                // Activating Event (Prompt 1)
                OutlinedTextField(
                    value = prompt1,
                    onValueChange = { prompt1 = it },
                    label = { Text("Activating Event") },
                    isError = showError && prompt1.text.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPrompt1)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Beliefs (Prompt 2)
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

                // Consequences (Prompt 3)
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

                // Belief Rating Slider
                Text("Belief Rating")
                Slider(
                    value = beliefRating,
                    onValueChange = { beliefRating = it },
                    valueRange = 1f..10f,
                    steps = 9
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Consequence Rating Slider
                Text("Consequence Rating")
                Slider(
                    value = consequenceRating,
                    onValueChange = { consequenceRating = it },
                    valueRange = 1f..10f,
                    steps = 9
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                            // Save the form
                            val newForm = ThoughtFormEntity(
                                userUid = userUid,
                                title = title.text,
                                date = date,
                                prompt1 = prompt1.text,
                                prompt2 = prompt2.text,
                                prompt3 = prompt3.text,
                                beliefRating = beliefRating.toInt(),
                                consequenceRating = consequenceRating.toInt()
                            )
                            viewModel.saveForm(newForm)
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
