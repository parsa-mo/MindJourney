@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mindjourney.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
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
import java.util.Calendar

@Composable
fun SignupScreen(navController: NavHostController) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf("") }

    // Get context and current date
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Date picker dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            birthday.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            Log.d("DatePicker", "Selected date: ${birthday.value}")
        }, year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )

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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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

                Spacer(modifier = Modifier.height(12.dp))

                // Birthday Field with Date Picker
                OutlinedTextField(
                    value = birthday.value,
                    onValueChange = { /* No user input allowed directly */ },
                    label = { Text("Birthday", fontWeight = FontWeight.Bold) },
                    readOnly = true, // Make the field read-only so user cannot type
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
                        .clickable {
                            Log.d("DatePicker", "DatePicker clicked") // Log to confirm click
                            datePickerDialog.show() // Show the date picker when clicked
                        }
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password", fontWeight = FontWeight.Bold) },
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirm Password", fontWeight = FontWeight.Bold) },
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

                Row(){
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = { /* Handle login */ }) {
                        Text("Sign Up", color = Color(0xFF7D5AA0))
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        modifier = Modifier.padding(end = 10.dp),
                        onClick = {
                            navController.navigate("Home")
                        }) {
                        Text("Cancel", color = Color(0xFF7D5AA0))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    val navController = rememberNavController()
    SignupScreen(navController)
}
