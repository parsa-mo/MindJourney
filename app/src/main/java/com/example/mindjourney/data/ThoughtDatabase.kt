package com.example.mindjourney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Thought::class], version = 1)
abstract class ThoughtDatabase : RoomDatabase() {
    abstract fun thoughtDao(): ThoughtDao

    companion object {
        @Volatile
        private var INSTANCE: ThoughtDatabase? = null

        fun getDatabase(context: Context): ThoughtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThoughtDatabase::class.java,
                    "thought_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
