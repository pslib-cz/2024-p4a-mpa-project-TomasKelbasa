package com.example.bookshelf.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookWithCategories(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "categoryId",
        associateBy = Junction(BookCategoryCrossRef::class)
    )
    val categories: List<Category>
)
