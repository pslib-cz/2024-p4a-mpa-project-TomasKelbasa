package com.example.bookshelf.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface BookshelfDao{
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>
}

@Database(
    entities = [Book::class, Category::class, BookCategoryCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class BookshelfDatabase : RoomDatabase() {
    abstract fun bookshelfDao(): BookshelfDao
}