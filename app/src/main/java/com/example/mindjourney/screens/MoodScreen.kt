@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mindjourney.R
import com.example.mindjourney.components.BottomNavBar
import com.example.mindjourney.data.Mood
import com.example.mindjourney.data.ThoughtDatabase
import com.example.mindjourney.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MoodScreen(navController: NavHostController) {
    val context = LocalContext.current
    val moodDao = ThoughtDatabase.getDatabase(context).moodDao()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    var selectedMood by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId, currentDate) {
        scope.launch {
            val mood = moodDao.getMoodForDate(userId, currentDate)
            if (mood != null) {
                selectedMood = mood.moodRating
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Thought Diaries",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
                )

                Divider(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                    thickness = 3.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Mood Rating Scale
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Sad Emoji
                    Image(
                        painter = painterResource(id = R.drawable.sad_emoji), // Assume you have sad emoji drawable
                        contentDescription = "Sad",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Mood Rating Radio Buttons
                    for (i in 1..5) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RadioButton(
                                selected = selectedMood == i,
                                onClick = {
                                    selectedMood = i
                                    scope.launch {
                                        moodDao.insert(
                                            Mood(
                                                userId = userId,
                                                moodRating = i
                                            )
                                        )
                                    }
                                }
                            )
                            Text(
                                text = i.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Happy Emoji
                    Image(
                        painter = painterResource(id = R.drawable.happy_emoji), // Assume you have happy emoji drawable
                        contentDescription = "Happy",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mood Labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "", modifier = Modifier.width(40.dp)) // Placeholder for spacing
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Neutral", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "", modifier = Modifier.width(40.dp)) // Placeholder for spacing
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Success Message After Rating
                if (selectedMood != null) {
                    Text(
                        text = "Good job on rating your mood for another day!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Purple40,
                        modifier = Modifier.padding(top = 16.dp)

                    )
                }
            }
        }
    )
}
