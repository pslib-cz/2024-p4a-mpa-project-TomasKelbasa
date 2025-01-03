package com.example.bookshelf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.ui.theme.BookshelfTheme
import com.example.bookshelf.viewmodels.BookDetailViewModel
import com.example.bookshelf.views.BookDetailScreen
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState

class BookDetailActivity : ComponentActivity() {

    private val viewModel: BookDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookId = intent.getLongExtra(BOOK_ID_EXTRA, -1L)
        if (bookId == -1L) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d("Bookshelf","Book id is ${bookId.toString()}")
        viewModel.loadBookDetails(bookId)

        setContent {
            val book = viewModel.bookWithCategories.observeAsState()
            BookshelfTheme {
                Scaffold { padding ->
                    if (book.value != null) {
                        BookDetailScreen(
                            book = book.value!!,
                            modifier = Modifier.padding(padding),
                            onDeleteBookClick = { bookWithCategories -> deleteBookAndFinish(bookWithCategories)},
                            onEditBookClick = {}
                        )
                    }
                    else{
                        Text("Book not found")
                    }
                }
            }
        }
    }

    private fun deleteBookAndFinish(book: BookWithCategories) {
        lifecycleScope.launch {
            viewModel.deleteBook(book.book)
            Toast.makeText(this@BookDetailActivity, "Book deleted", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous screen
        }
    }

    companion object {
        private const val BOOK_ID_EXTRA = "book_id"

        fun newIntent(context: Context, bookId: Long): Intent {
            return Intent(context, BookDetailActivity::class.java).apply {
                putExtra(BOOK_ID_EXTRA, bookId)
            }
        }
    }
}

