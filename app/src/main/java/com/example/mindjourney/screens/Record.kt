package com.example.mindjourney.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mindjourney.components.BottomNavBar

@Composable
fun RecordScreen(navController: NavController) {
    var prompt1 by remember { mutableStateOf("") }
    var prompt2 by remember { mutableStateOf("") }
    var prompt3 by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController) // Pass the navController to the BottomNavBar
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Prompts with text fields
                Text(text = "Activating Event: ", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "This may include an actual event or a situation, a thought, mental picture or physical trigger.",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = prompt1,
                    onValueChange = { prompt1 = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))


                Text(text = "Beliefs: ", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "1. List all self-statements that link A to C. Ask yourself: “What was I" +
                            "thinking?” “What was I saying to myself?” “What was going through" +
                            "my head at the time?”",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "2. Find the most distressing (hot) thought and mark it with an asterisk (*)",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "3. Rate how much you believe this thought between 0 to 100.",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = prompt2,
                    onValueChange = { prompt2 = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))


                Text(text = "Consequences: ", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "1. Write down words describing how you feel.",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "2. Mark the one that is most associated with the activating" +
                            " event using an asterisk (*).",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "3. Rate the intensity of this feeling between" +
                            "0 to 100",
                    style = TextStyle(fontSize = 15.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = prompt3,
                    onValueChange = { prompt3 = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Save and Cancel buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Handle Save action
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp) // Optional padding between buttons
                    ) {
                        Text(text = "Save", fontSize = 18.sp)
                    }

                    Button(
                        onClick = {
                            // Handle Cancel action
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(text = "Cancel", fontSize = 18.sp)
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordScreen() {
    // Use a dummy NavController for preview purposes
    val dummyNavController = rememberNavController()
    RecordScreen(navController = dummyNavController)
}
