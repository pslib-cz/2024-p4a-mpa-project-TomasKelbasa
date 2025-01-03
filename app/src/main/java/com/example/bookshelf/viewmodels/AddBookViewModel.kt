package com.example.bookshelf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookCategoryCrossRef
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.data.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddBookViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BookshelfDatabase.getInstance(application).bookshelfDao()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        viewModelScope.launch {
            _categories.value = dao.getAllCategories()
        }
    }

    internal fun getAllCategories(): List<Category> {
        var categories = emptyList<Category>()
        viewModelScope.launch {
            categories = dao.getAllCategories()
        }
        return categories
    }

    internal fun addBook(book: Book, categoryIds: List<Long>){
        viewModelScope.launch {
            val bookId = dao.insertBook(book)
            categoryIds.forEach { categoryId ->
                dao.insertBookCategoryCrossRef(BookCategoryCrossRef(bookId, categoryId))
            }
        }
    }
}