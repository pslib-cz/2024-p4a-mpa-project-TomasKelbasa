package com.example.bookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDao
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.ui.theme.BookshelfTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

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
                val books = dao.getAllBooksFlow().collectAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize().padding(5.dp, 45.dp, 5.dp, 5.dp)) { innerPadding ->
                    Box{
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
                            BookList(books = books.value, modifier = Modifier.padding(innerPadding), onBookClick = { Intent()})
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
fun BookList(books: List<Book>, modifier: Modifier = Modifier, onBookClick: () -> Unit) {
    LazyColumn {
        items(books) { book ->
            BookCard(book, onClick = { onBookClick() })
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
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

