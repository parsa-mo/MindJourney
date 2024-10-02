package com.example.mindjourney.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mindjourney.R // Make sure this imports your drawable resource correctly
import com.example.mindjourney.components.BottomNavBar

@Composable
fun DashboardScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController) // Pass the navController to the BottomNavBar
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Mood Fluctuations ",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Chart Image at the top
                Image(
                    painter = painterResource(id = R.drawable.chart), // Reference to your chart image in drawable
                    contentDescription = "Mood Chart",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Section (Row with different boxes)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatBox(title = "Daily Diary Streak", stat = "5 Days")
                    StatBox(title = "Total Diaries", stat = "12")
                    StatBox(title = "Mood Today", stat = "Happy")
                }
            }
        }
    )
}

@Composable
fun StatBox(title: String, stat: String) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF7D5AA0)) // Correct parameter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stat, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    val navController = rememberNavController()
    DashboardScreen(navController)
}
