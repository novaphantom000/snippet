package com.example.snippet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snippets")
data class SnippetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val category: String,
    val colorHex: String,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
