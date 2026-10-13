package com.example.snippet.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SnippetDao {
    @Query("SELECT * FROM snippets ORDER BY isPinned DESC, timestamp DESC")
    fun getAllSnippets(): Flow<List<SnippetEntity>>

    @Query("SELECT * FROM snippets WHERE title LIKE :query OR content LIKE :query OR category LIKE :query ORDER BY isPinned DESC, timestamp DESC")
    fun searchSnippets(query: String): Flow<List<SnippetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnippet(snippet: SnippetEntity)

    @Update
    suspend fun updateSnippet(snippet: SnippetEntity)

    @Delete
    suspend fun deleteSnippet(snippet: SnippetEntity)

    @Query("UPDATE snippets SET isPinned = :pinned WHERE id = :id")
    suspend fun updatePinStatus(id: Int, pinned: Boolean)

    @Query("UPDATE snippets SET isFavorite = :favorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, favorite: Boolean)
}
