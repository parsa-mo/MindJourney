package com.example.mindjourney.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "mood_table")
data class Mood(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val userId: String,
        val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
val moodRating: Int
)

