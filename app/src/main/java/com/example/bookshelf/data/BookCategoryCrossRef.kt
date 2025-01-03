package com.example.bookshelf.data

import androidx.room.Entity

@Entity(primaryKeys = ["bookId", "categoryId"], tableName = "bookCategoryCrossRef")
data class BookCategoryCrossRef(
    val bookId: Long,
    val categoryId: Long
)
