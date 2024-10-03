@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import android.app.Activity
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mindjourney.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterConfirmPassword = remember { FocusRequester() }

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

        // Card for Signup Form
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
                // Name Input
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                        errorMessage.value = null // Clear error when user types
                    },
                    label = { Text("Name", fontWeight = FontWeight.Bold) },
                    isError = errorMessage.value?.contains("name", ignoreCase = true) == true,
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
                        .focusRequester(focusRequesterName)
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    label = { Text("Password", color = Color.White, fontWeight = FontWeight.Bold) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Input
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = {
                        confirmPassword.value = it
                        errorMessage.value = null // Clear error when user types
                    },
                    label = { Text("Confirm Password", color = Color.White, fontWeight = FontWeight.Bold) },
                    isError = errorMessage.value?.contains("confirm password", ignoreCase = true) == true,
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
                        .focusRequester(focusRequesterConfirmPassword)
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

                // Sign Up Button
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (name.value.isEmpty()) {
                            errorMessage.value = "Please enter your name."
                            focusRequesterName.requestFocus()
                            return@Button
                        }
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
                        if (confirmPassword.value.isEmpty()) {
                            errorMessage.value = "Please confirm your password."
                            focusRequesterConfirmPassword.requestFocus()
                            return@Button
                        }
                        if (password.value != confirmPassword.value) {
                            errorMessage.value = "Passwords do not match."
                            focusRequesterPassword.requestFocus()
                            return@Button
                        }
                        isLoading.value = true
                        signUpWithEmailPassword(name.value, email.value, password.value, context as Activity, navController, isLoading)
                    }
                ) {
                    Text("Sign Up", color = Color(0xFF7D5AA0))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back to Login Button
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("login")
                    }
                ) {
                    Text("Back to Login", color = Color(0xFF7D5AA0))
                }
            }
        }
    }
}

fun signUpWithEmailPassword(
    name: String,
    email: String,
    password: String,
    activity: Activity,
    navController: NavHostController,
    isLoading: MutableState<Boolean>
) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            isLoading.value = false // Stop loading
            if (task.isSuccessful) {
                // User creation was successful, now set the display name
                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = name // Set the display name
                }

                user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        Toast.makeText(activity, "Sign Up successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") // Navigate to login after sign-up
                    } else {
                        Toast.makeText(activity, "Failed to update profile: ${profileTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, "Sign Up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    SignupScreen(rememberNavController())
}
