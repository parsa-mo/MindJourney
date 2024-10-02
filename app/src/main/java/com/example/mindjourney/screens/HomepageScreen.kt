package com.example.mindjourney.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mindjourney.R
import com.example.mindjourney.ui.theme.Purple40

@Composable
fun HomepageScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Welcome to Your \n Mind Journey",
            style = typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Gray

        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = { navController.navigate("Signup") },
            colors = ButtonDefaults.buttonColors(
                Purple40
            )
        ){
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {navController.navigate("Login")},
            colors = ButtonDefaults.buttonColors(
                Purple40
            )
        ){
            Text("Log in")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomepageScreen() {
    val navController = rememberNavController()
    HomepageScreen(navController)
}