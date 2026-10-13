package com.example.snippet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SnippetEntity::class], version = 1, exportSchema = false)
abstract class SnippetDatabase : RoomDatabase() {
    abstract fun snippetDao(): SnippetDao

    companion object {
        @Volatile
        private var INSTANCE: SnippetDatabase? = null

        fun getDatabase(context: Context): SnippetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SnippetDatabase::class.java,
                    "snippet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
