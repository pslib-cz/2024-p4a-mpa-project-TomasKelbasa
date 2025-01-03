package com.example.bookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = BookshelfDatabase.getInstance(this)
        dao = db.bookshelfDao()

        enableEdgeToEdge()
        setContent {
            BookshelfTheme {
                var books by remember { mutableStateOf<List<Book>>(emptyList()) }
                var selectedCategory by remember { mutableStateOf<Category?>(null) }
                var categories by remember { mutableStateOf<List<Category>>(emptyList()) }

                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        categories = dao.getAllCategories()
                        books = dao.getAllBooks()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize().padding(5.dp, 45.dp, 5.dp, 5.dp),
                    ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()){
                        FloatingActionButton(
                            onClick = {
                                startActivity(Intent(this@MainActivity, AddBookActivity::class.java))
                            },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).zIndex(100.0f)
                        ){
                            Text("+", fontSize = 30.sp)
                        }
                        Column {
                            Text(text = "My Bookshelf", style = MaterialTheme.typography.headlineLarge)
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


    private fun logBooksWithCategories(booksWithCategories: List<BookWithCategories>) {
        booksWithCategories.forEach { bookWithCategories ->
            Log.d(
                "Bookshelf",
                "Book: ${bookWithCategories.book.title}, Categories: ${
                    bookWithCategories.categories.joinToString { it.name }
                }"
            )
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

