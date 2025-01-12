package com.example.bookshelf

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDao
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.data.Category
import com.example.bookshelf.ui.theme.BookshelfTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var db: BookshelfDatabase
    private lateinit var dao: BookshelfDao
    private var _books = mutableStateOf<List<Book>>(emptyList())
    private var _selectedCategory = mutableStateOf<Category?>(null)
    private var _categories = mutableStateOf<List<Category>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = BookshelfDatabase.getInstance(this)
        dao = db.bookshelfDao()

        enableEdgeToEdge()
        setContent {
            BookshelfTheme {
                var books by remember { _books }
                var selectedCategory by remember { _selectedCategory }
                var categories by remember { _categories }

                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        categories = dao.getAllCategories()
                        books = dao.getAllBooks()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                startActivity(Intent(this@MainActivity, AddBookActivity::class.java))
                            },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).zIndex(100.0f)
                        ){
                            Icon(imageVector = Icons.Default.Add, "Add new book")
                        }
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "My Bookshelf", style = MaterialTheme.typography.headlineLarge)
                                IconButton(
                                    onClick = {
                                        startActivity(Intent(this@MainActivity, ManageCategoriesActivity::class.java))
                                    },
                                    modifier = Modifier.padding(0.dp).background(Color.Transparent)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        modifier = Modifier.padding(0.dp).background(Color.Transparent)
                                    )
                                }
                            }
                            CategoryFilterDropdown(
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { category ->
                                    selectedCategory = category
                                    lifecycleScope.launch {
                                        if(category == null) books = dao.getAllBooks()
                                        else books = dao.getBooksByCategory(category.categoryId)
                                    }
                                }
                            )
                            BookList(
                                books = books,
                                modifier = Modifier.padding(innerPadding),
                                onBookClick = { book ->
                                    startActivity(BookDetailActivity.newIntent(this@MainActivity, book.bookId))
                                })
                        }
                    }
                }
            }
        }
    }

    // Update _books on resume (book may have been deleted or added)
    // Update _categories on resume (category may have been deleted or added)
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val dao = db.bookshelfDao()
            _categories.value = dao.getAllCategories()

            if(_selectedCategory.value == null) {
                _books.value = dao.getAllBooks()
            }
            else {
                _books.value = dao.getBooksByCategory(_selectedCategory.value!!.categoryId)
            }
        }
    }
}

@Composable
fun BookList(books: List<Book>, modifier: Modifier = Modifier, onBookClick: (book: Book) -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookCard(book, onClick = { onBookClick(book) })
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
        onClick = { onClick() }
    ) {
        Column {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(16.dp,16.dp,16.dp,0.dp)
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CategoryFilterDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("All") }

    Box(modifier = Modifier.padding(8.dp)) {
        Button(
            onClick = { expanded = true },
        ) {
            Text(selectedText)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    expanded = false
                    selectedText = "All"
                    onCategorySelected(null)
                }
            )
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        expanded = false
                        selectedText = category.name
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}

