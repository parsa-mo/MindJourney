package com.example.mindjourney.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mood: Mood)

    @Query("SELECT * FROM mood_table WHERE userId = :userId AND date = :date LIMIT 1")
    suspend fun getMoodForDate(userId: String, date: String): Mood?
}
