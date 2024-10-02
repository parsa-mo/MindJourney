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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mindjourney.screens.RecordScreen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf("dashboard", "mood", "record", "thoughts", "account")

    // Find out the current destination from the NavController
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF7D5AA0),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination == "dashboard",
            onClick = {
                if (currentDestination != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Face, contentDescription = "Mood") },
            label = { Text("Mood") },
            selected = currentDestination == "mood",
            onClick = {
                if (currentDestination != "mood") {
                    navController.navigate("mood") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Record") },
            label = { Text("Record") },
            selected = currentDestination == "record",
            onClick = {
                if (currentDestination != "record") {
                    navController.navigate("record") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "Thoughts") },
            label = { Text("Thoughts") },
            selected = currentDestination == "thoughts",
            onClick = {
                if (currentDestination != "thoughts") {
                    navController.navigate("thoughts") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Account") },
            label = { Text("Account") },
            selected = currentDestination == "account",
            onClick = {
                if (currentDestination != "account") {
                    navController.navigate("account") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
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
