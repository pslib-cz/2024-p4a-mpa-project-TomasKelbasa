package com.example.bookshelf.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookCategoryCrossRef
import com.example.bookshelf.data.BookWithCategories
import com.example.bookshelf.data.BookshelfDatabase
import com.example.bookshelf.data.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditBookViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = BookshelfDatabase.getInstance(application).bookshelfDao()
    private val _bookWithCategories = MutableLiveData<BookWithCategories?>()
    val bookWithCategories: LiveData<BookWithCategories?> = _bookWithCategories
    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>> = _categories



    fun loadBookDetails(bookId: Long) {
        viewModelScope.launch {
            val book = dao.getBookWithCategories(bookId)
            _bookWithCategories.postValue(book)
        }
    }

    fun loadCategories(){
        viewModelScope.launch {
            val categories = dao.getAllCategories()
            _categories.postValue(categories)
        }
    }

    internal fun validate(book: Book): Boolean{
        if (book.title.isEmpty()) return false
        if (book.author.isEmpty()) return false
        return true
    }

    internal fun updateBook(book: Book, categoryIds: List<Long>){
        viewModelScope.launch {
            val bookId = dao.insertBook(book)
            dao.deleteBookCrossRefs(bookId)
            categoryIds.forEach { categoryId ->
                dao.insertBookCategoryCrossRef(BookCategoryCrossRef(bookId, categoryId))
            }
        }
    }

    internal suspend fun updateBookSuspended(book: Book, categoryIds: List<Long>){
        val bookId = dao.insertBook(book)
        dao.deleteBookCrossRefs(bookId)
        categoryIds.forEach { categoryId ->
            dao.insertBookCategoryCrossRef(BookCategoryCrossRef(bookId, categoryId))
        }
    }
}
