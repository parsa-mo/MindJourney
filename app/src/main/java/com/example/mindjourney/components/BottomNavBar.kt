package com.example.mindjourney.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = Color(0xFF7D5AA0),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                navController.navigate("dashboard")
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Face, contentDescription = "Mood") },
            label = { Text("Mood") },
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Record") },
            label = { Text("Record") },
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "Thoughts") },
            label = { Text("Thoughts") },
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Account") },
            label = { Text("Account") },
            selected = selectedIndex == 4,
            onClick = { selectedIndex = 4 }
        )
    }
}

fun getScreenLabel(index: Int): String {
    return when (index) {
        0 -> "Home"
        1 -> "Mood"
        2 -> "Record"
        3 -> "Thoughts"
        4 -> "Account"
        else -> "Home"
    }
}
