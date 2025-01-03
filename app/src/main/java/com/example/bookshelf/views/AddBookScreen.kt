package com.example.bookshelf.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    categories: List<Category>,
    onSaveBook: (Book, List<Long>) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var author by remember { mutableStateOf(TextFieldValue("")) }
    var pages by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var rating by remember { mutableStateOf(TextFieldValue("")) }
    val selectedCategories = remember { mutableStateListOf<Long>() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Book") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = pages,
                onValueChange = { pages = it },
                label = { Text("Pages") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = rating,
                onValueChange = { rating = it },
                label = { Text("Rating (1-5)") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Categories", style = MaterialTheme.typography.titleMedium)
            categories.forEach { category ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedCategories.contains(category.categoryId),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedCategories.add(category.categoryId)
                            } else {
                                selectedCategories.remove(category.categoryId)
                            }
                        }
                    )
                    Text(text = category.name)
                }
            }
            Button(
                onClick = {
                    val book = Book(
                        title = title.text,
                        author = author.text,
                        pages = pages.text.toIntOrNull() ?: 0,
                        description = description.text.takeIf { it.isNotBlank() },
                        rating = rating.text.toIntOrNull() ?: 1
                    )
                    onSaveBook(book, selectedCategories)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Book")
            }
        }
    }
}

