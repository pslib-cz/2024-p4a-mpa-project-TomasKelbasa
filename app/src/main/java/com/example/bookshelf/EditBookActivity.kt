package com.example.bookshelf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.data.Category
import com.example.bookshelf.ui.theme.BookshelfTheme
import com.example.bookshelf.viewmodels.EditBookViewModel
import com.example.bookshelf.views.EditBookScreen
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditBookActivity : ComponentActivity() {

    private val viewModel: EditBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookId = intent.getLongExtra(EditBookActivity.BOOK_ID_EXTRA, -1L)
        if (bookId == -1L) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.loadBookDetails(bookId)
        viewModel.loadCategories()

        setContent {
            val book = viewModel.bookWithCategories.observeAsState()
            val categories = viewModel.categories.observeAsState()
            BookshelfTheme {
                if (book.value == null || categories.value == null) {
                    Text("Loading...")
                } else {
                    EditBookScreen(
                        currentBook = book.value!!,
                        categories = categories.value!!,
                        onSaveBook = { book, categoryIds ->
                            if (!viewModel.validate(book)) {
                                Toast.makeText(this, "Invalid book", Toast.LENGTH_SHORT).show()
                            } else {
                                runBlocking {
                                    viewModel.updateBookSuspended(book, categoryIds)
                                }
                                Toast.makeText(this, "Book updated!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    )
                }
            }
        }

    }

    companion object {
        private const val BOOK_ID_EXTRA = "book_id"

        fun newIntent(context: Context, bookId: Long): Intent {
            return Intent(context, EditBookActivity::class.java).apply {
                putExtra(BOOK_ID_EXTRA, bookId)
            }
        }
    }
}