package com.example.mindjourney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Thought::class, Mood::class], version = 2, exportSchema = false)
abstract class ThoughtDatabase : RoomDatabase() {

    abstract fun thoughtDao(): ThoughtDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: ThoughtDatabase? = null

        fun getDatabase(context: Context): ThoughtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThoughtDatabase::class.java,
                    "thought_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
