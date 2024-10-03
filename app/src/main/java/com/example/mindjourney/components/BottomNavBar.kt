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

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf("dashboard", "mood", "record", "thoughts", "account")

    val currentBackStack = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStack?.destination?.route

    if (currentDestination == null) {
        // Log or handle the case where currentDestination is null
        return
    }

    NavigationBar(
        containerColor = Color(0xFF7D5AA0),
        contentColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        "dashboard" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "mood" -> Icon(Icons.Filled.Face, contentDescription = "Mood")
                        "record" -> Icon(Icons.Filled.AddCircle, contentDescription = "Record")
                        "thoughts" -> Icon(Icons.Filled.Star, contentDescription = "Thoughts")
                        "account" -> Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
                    }
                },
                label = { Text(screen.capitalize()) },
                selected = currentDestination == screen,
                onClick = {
                    if (currentDestination != screen) {
                        navController.navigate(screen) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}