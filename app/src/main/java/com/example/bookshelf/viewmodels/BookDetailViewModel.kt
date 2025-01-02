package com.example.bookshelf.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.*
import kotlinx.coroutines.launch

class BookDetailViewModel(private val dao: BookshelfDao) : ViewModel() {
    private val _allBooks = MutableLiveData<List<Book>>()
    val allBooks: LiveData<List<Book>> = _allBooks

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            dao.deleteBook(book)
            loadBooks() // Obnovit seznam knih
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            _allBooks.value = dao.getAllBooks()
        }
    }
}