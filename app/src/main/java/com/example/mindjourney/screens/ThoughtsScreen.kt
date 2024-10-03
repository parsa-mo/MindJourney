import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.components.ThoughtFormViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ThoughtsScreen(navController: NavController, viewModel: ThoughtFormViewModel = viewModel()) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userUid = currentUser?.uid ?: ""

    // Fetch user-specific forms from the ViewModel
    val formList = viewModel.getUserForms(userUid).collectAsState(initial = emptyList()).value

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
                    text = "Saved Thought Diaries",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )

                Divider(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                    thickness = 3.dp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(formList) { form ->
                        ExpandableThoughtFormCard(form = form, viewModel = viewModel)
                    }
                }
            }
        }
    )
}

@Composable
fun ExpandableThoughtFormCard(form: ThoughtFormEntity, viewModel: ThoughtFormViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteForm(form)  // Delete the form
                        showDeleteDialog = false  // Close dialog
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

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = form.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = form.date, style = MaterialTheme.typography.bodyMedium)
                }

                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Gray)
                }
            }

            if (isExpanded) {
                Text("Activating Event", style = MaterialTheme.typography.titleMedium)
                Text(text = form.prompt1)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Beliefs", style = MaterialTheme.typography.titleMedium)
                Text(text = form.prompt2)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Consequences", style = MaterialTheme.typography.titleMedium)
                Text(text = form.prompt3)
            }
        }
    }
}
