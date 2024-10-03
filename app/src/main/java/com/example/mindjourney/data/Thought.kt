package com.example.mindjourney.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thoughts")
data class Thought(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val title: String,
    val prompt1: String,
    val prompt2: String,
    val prompt3: String,
    val beliefRating: Int,
    val consequenceRating: Int,
    val date: String
)