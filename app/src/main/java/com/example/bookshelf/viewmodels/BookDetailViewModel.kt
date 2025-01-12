package com.example.bookshelf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.*
import kotlinx.coroutines.launch

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = BookshelfDatabase.getInstance(application).bookshelfDao()
    private val _bookWithCategories = MutableLiveData<BookWithCategories?>()
    val bookWithCategories: LiveData<BookWithCategories?> = _bookWithCategories

    fun loadBookDetails(bookId: Long) {
        viewModelScope.launch {
            val book = dao.getBookWithCategories(bookId)
            _bookWithCategories.postValue(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            dao.deleteBook(book)
        }
    }
}