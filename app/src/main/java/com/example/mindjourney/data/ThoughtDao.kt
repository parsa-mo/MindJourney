package com.example.mindjourney.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ThoughtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(thought: Thought)

    @Update
    suspend fun update(thought: Thought)

    @Delete
    suspend fun delete(thought: Thought)

    @Query("SELECT * FROM thoughts WHERE userId = :userId ORDER BY date DESC")
    fun getThoughtsForUser(userId: String): LiveData<List<Thought>>
}