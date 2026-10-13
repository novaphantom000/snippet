package com.example.snippet.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snippet.viewmodel.SnippetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: SnippetViewModel = viewModel()
    val snippets by viewModel.snippets.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Snippet", style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Snippet")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search snippets...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            if (snippets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No snippets found", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(snippets) { snippet ->
                        SnippetCard(
                            snippet = snippet,
                            onPinToggle = { viewModel.togglePin(snippet) },
                            onFavoriteToggle = { viewModel.toggleFavorite(snippet) },
                            onDelete = { viewModel.deleteSnippet(snippet) },
                            onEdit = { /* Implement Edit */ }
                        )
                    }
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
                SnippetEditor(
                    onSave = { title, content, category, color ->
                        viewModel.saveSnippet(title, content, category, color)
                        showSheet = false
                    },
                    onDismiss = { showSheet = false }
                )
            }
        }
    }
}

@Composable
fun SnippetEditor(onSave: (String, String, String, String) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var selectedColor by remember { mutableStateOf("#6750A4") }

    val colors = listOf("#6750A4", "#E91E63", "#4CAF50", "#FFC107", "#2196F3", "#9C27B0")

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(bottom = 48.dp)
            .fillMaxWidth()
    ) {
        Text("New Snippet", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Category Color", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(android.graphics.Color.parseColor(color)), RoundedCornerShape(16.dp))
                        .clickable { selectedColor = color }
                        .border(if (selectedColor == color) 2.dp else 0.dp, Color.Black, RoundedCornerShape(16.dp))
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onSave(title, content, category, selectedColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Snippet")
        }
    }
}
