package com.example.mindjourney.screens

data class User(
    val name: String,
    val email: String,
    val profilePictureUrl: String? // Nullable for cases without a profile picture
)