package com.example.bookshelf

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDao
import com.example.bookshelf.data.BookshelfDatabase
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Button(onClick = {
                        lifecycleScope.launch {
                            val booksWithCategories = dao.getAllBooksWithCategories()
                            logBooksWithCategories(booksWithCategories)
                        }
                    },
                    ){
                        Text("Get Books")
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookshelfTheme {
        Greeting("Android")
    }
}