package com.example.bookshelf.data

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val bookId: Long = 0,
    val title: String,
    val author: String,
    val pages: Int,
    val description: String? = null,
    val rating: Int
)