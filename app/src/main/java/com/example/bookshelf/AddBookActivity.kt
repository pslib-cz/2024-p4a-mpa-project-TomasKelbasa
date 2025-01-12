package com.example.bookshelf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.bookshelf.ui.theme.BookshelfTheme
import com.example.bookshelf.viewmodels.AddBookViewModel
import com.example.bookshelf.views.AddBookScreen

class AddBookActivity : ComponentActivity() {

    private val viewModel: AddBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookshelfTheme {
                val categories by viewModel.categories.collectAsState(initial = emptyList())
                AddBookScreen(
                    categories = categories,
                    onSaveBook = { book, categoryIds ->
                        if (!viewModel.validate(book)){
                            Toast.makeText(this, "Invalid book", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            viewModel.addBook(book, categoryIds)
                            Toast.makeText(this, "Book added!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                )
            }
        }
    }
}
