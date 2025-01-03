package com.example.bookshelf.data
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction

data class CategoryWithBooks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "bookId",
        associateBy = Junction(BookCategoryCrossRef::class)
    )
    val books: List<Book>
)
