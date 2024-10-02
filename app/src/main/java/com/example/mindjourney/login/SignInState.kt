package com.example.mindjourney.login

data class SignInState(
  val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)