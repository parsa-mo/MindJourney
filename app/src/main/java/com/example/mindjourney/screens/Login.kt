@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import android.app.Activity
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import kotlinx.coroutines.launch

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
    val errorMessage = remember { mutableStateOf<String?>(null) }

    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()

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
                    onValueChange = {
                        email.value = it
                        errorMessage.value = null // Clear error when user types
                    },
                    label = { Text("Email", fontWeight = FontWeight.Bold) },
                    isError = errorMessage.value?.contains("email", ignoreCase = true) == true,
                    textStyle = LocalTextStyle.current.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterEmail)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        errorMessage.value = null // Clear error when user types
                    },
                    label = { Text("Password", fontWeight = FontWeight.Bold, color = Color.White) },
                    isError = errorMessage.value?.contains("password", ignoreCase = true) == true,
                    textStyle = LocalTextStyle.current.copy(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesterPassword)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Error Message Text
                if (errorMessage.value != null) {
                    Text(
                        text = errorMessage.value!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            if (email.value.isEmpty()) {
                                errorMessage.value = "Please enter your email."
                                focusRequesterEmail.requestFocus()
                                return@Button
                            }
                            if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                errorMessage.value = "Please enter a valid email address."
                                focusRequesterEmail.requestFocus()
                                return@Button
                            }
                            if (password.value.isEmpty()) {
                                errorMessage.value = "Please enter your password."
                                focusRequesterPassword.requestFocus()
                                return@Button
                            }

                            // Sign in with email and password
                            isLoading.value = true
                            coroutineScope.launch {
                                signInWithEmailPassword(
                                    auth,
                                    email.value,
                                    password.value,
                                    context as Activity,
                                    navController,
                                    isLoading,
                                    { user ->
                                        // Handle successful login here, e.g., navigate to the dashboard or show user info
                                        Toast.makeText(context, "Welcome, ${user.name}!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("dashboard")
                                    },
                                    { error ->
                                        errorMessage.value = error
                                    }
                                )
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
    onLoginSuccess: (User) -> Unit, // Callback to handle successful login
    onLoginFailure: (String) -> Unit // Callback to handle login failure
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            isLoading.value = false // Stop loading
            if (task.isSuccessful) {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    val userId = currentUser.uid
                    val userName = currentUser.displayName ?: "User"
                    val userEmail = currentUser.email ?: ""

                    val user = User(name = userName, email = userEmail, profilePictureUrl = null)

                    Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(user)
                }
            } else {
                val errorMessage = when {
                    task.exception?.message?.contains("password") == true -> "Incorrect password."
                    task.exception?.message?.contains("no user record") == true -> "Account does not exist."
                    else -> "Login failed: ${task.exception?.message}"
                }
                onLoginFailure(errorMessage)
            }
        }
}
