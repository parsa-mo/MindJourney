@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mindjourney.MainActivity
import com.example.mindjourney.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    auth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )

        // Card for Login Form
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7D5AA0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email Input
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email", fontWeight = FontWeight.Bold) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password", color = Color.White, fontWeight = FontWeight.Bold) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            if (email.value.isEmpty() || password.value.isEmpty()) {
                                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            isLoading.value = true
                            signInWithEmailPassword(
                                auth,
                                email.value,
                                password.value,
                                context as Activity,
                                navController,
                                isLoading
                            ) { user ->
                                // Handle successful login here, e.g., navigate to the dashboard or show user info
                                Toast.makeText(context, "Welcome, ${user.name}!", Toast.LENGTH_SHORT).show()
                                navController.navigate("dashboard")
                            }
                        }
                    ) {
                        Text("Sign In", color = Color(0xFF7D5AA0))
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            navController.navigate("home")
                        }
                    ) {
                        Text("Cancel", color = Color(0xFF7D5AA0))
                    }
                }
            }
        }

        // Google Sign-In Card
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7D5AA0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.googlebtn),
                    contentDescription = "Google Sign In Button",
                    modifier = Modifier
                        .height(45.dp)
                        .clickable {
                            val signInIntent = googleSignInClient.signInIntent
                            (context as Activity).startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
                        }
                )
            }
        }
    }
}

fun signInWithEmailPassword(
    auth: FirebaseAuth,
    email: String,
    password: String,
    activity: Activity,
    navController: NavHostController,
    isLoading: MutableState<Boolean>,
    onLoginSuccess: (User) -> Unit // Callback to handle successful login
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            isLoading.value = false // Stop loading
            if (task.isSuccessful) {
                val currentUser = auth.currentUser

                // Log the current user details
                if (currentUser != null) {
                    // Access and log specific user properties
                    val userId = currentUser.uid
                    val userName = currentUser.displayName ?: "No display name"
                    val userEmail = currentUser.email ?: "No email"

                    Log.d("Login", "User ID: $userId") // Log the user ID
                    Log.d("Login", "User Name: $userName") // Log the display name
                    Log.d("Login", "User Email: $userEmail") // Log the email

                    // Create user object and proceed
                    val user = User(name = userName, email = userEmail, profilePictureUrl = null)

                    Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(user) // Call the callback with the user object
                } else {
                    Toast.makeText(
                        activity,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
}

