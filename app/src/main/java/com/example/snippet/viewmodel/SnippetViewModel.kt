package com.example.snippet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.snippet.data.SnippetDao
import com.example.snippet.data.SnippetDatabase
import com.example.snippet.data.SnippetEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SnippetViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = SnippetDatabase.getDatabase(application).snippetDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val snippets: StateFlow<List<SnippetEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                dao.getAllSnippets()
            } else {
                dao.searchSnippets("%$query%")
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun saveSnippet(title: String, content: String, category: String, colorHex: String, id: Int = 0) {
        viewModelScope.launch {
            val snippet = SnippetEntity(id = id, title = title, content = content, category = category, colorHex = colorHex)
            dao.insertSnippet(snippet)
        }
    }

    fun deleteSnippet(snippet: SnippetEntity) {
        viewModelScope.launch {
            dao.deleteSnippet(snippet)
        }
    }

    fun togglePin(snippet: SnippetEntity) {
        viewModelScope.launch {
            dao.updatePinStatus(snippet.id, !snippet.isPinned)
        }
    }

    fun toggleFavorite(snippet: SnippetEntity) {
        viewModelScope.launch {
            dao.updateFavoriteStatus(snippet.id, !snippet.isFavorite)
        }
    }
}
