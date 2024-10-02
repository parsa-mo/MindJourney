@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import android.app.Activity
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
                    onValueChange = { name.value = it },
                    label = { Text("Name", fontWeight = FontWeight.Bold) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Input
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirm Password", color = Color.White, fontWeight = FontWeight.Bold) },
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

                // Sign Up Button
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (name.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (password.value != confirmPassword.value) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
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
