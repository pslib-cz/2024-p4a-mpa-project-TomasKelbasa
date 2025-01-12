package com.example.bookshelf.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    categories: List<Category>,
    currentBook: BookWithCategories,
    onSaveBook: (Book, List<Long>) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(currentBook?.book?.title ?: "")) }
    var author by remember { mutableStateOf(TextFieldValue(currentBook?.book?.author ?: "")) }
    var pages by remember { mutableStateOf(TextFieldValue(currentBook?.book?.pages.toString() ?: "")) }
    var description by remember { mutableStateOf(TextFieldValue(currentBook?.book?.description ?: "")) }
    var rating by remember { mutableStateOf(TextFieldValue(currentBook?.book?.rating.toString())) }
    val selectedCategories = remember { mutableStateListOf<Long>()
        .apply {
            addAll(currentBook?.categories?.map { it.categoryId } ?: emptyList())
        }}

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit") })
        }
    ) { innerPadding ->
        Text(
            text = currentBook.book.title,
            style = MaterialTheme.typography.headlineMedium,
        )
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
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if(it.text.isEmpty() || it.text.matches(Regex("\\d*"))) pages = it
                },
                label = { Text("Number of pages") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
            TextField(
                value = rating,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if(it.text.isEmpty() || it.text.matches(Regex("[0-5]"))) rating = it
                },
                label = { Text("Rating (0-5)") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Categories", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.6F)
            ) {
                items(categories) { category ->
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = selectedCategories.contains(category.categoryId),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedCategories.add(category.categoryId)
                                } else {
                                    selectedCategories.remove(category.categoryId)
                                }
                            },
                        )
                        Text(text = category.name)
                    }
                }
            }
            Button(
                onClick = {
                    val book = Book(
                        bookId = currentBook?.book?.bookId ?: 0,
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

