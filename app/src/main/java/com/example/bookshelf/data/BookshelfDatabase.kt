package com.example.bookshelf.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface BookshelfDao{
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>
    @Query("SELECT * FROM books")
    fun getAllBooksFlow(): Flow<List<Book>>
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCategoryCrossRef(crossRef: BookCategoryCrossRef)
    @Transaction
    @Query("SELECT * FROM books WHERE bookId = :bookId")
    suspend fun getBookWithCategories(bookId: Long): BookWithCategories
    @Transaction
    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategoryWithBooks(categoryId: Long): CategoryWithBooks
    @Delete
    suspend fun deleteBook(book: Book)
    @Delete
    suspend fun deleteCategory(category: Category)
    @Query("DELETE FROM bookcategorycrossref WHERE bookId = :bookId AND categoryId = :categoryId")
    suspend fun deleteBookCategoryCrossRef(bookId: Long, categoryId: Long)

    @Transaction
    @Query("SELECT * FROM books")
    suspend fun getAllBooksWithCategories(): List<BookWithCategories>

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getAllCategoriesWithBooks(): List<CategoryWithBooks>
}

@Database(
    entities = [Book::class, Category::class, BookCategoryCrossRef::class],
    version = 1,
)
abstract class BookshelfDatabase : RoomDatabase() {
    abstract fun bookshelfDao(): BookshelfDao

    companion object {
        @Volatile
        private var INSTANCE: BookshelfDatabase? = null

        fun getInstance(context: Context): BookshelfDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookshelfDatabase::class.java,
                    "bookshelf_database"
                )
                    .addCallback(SeedDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class SeedDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Seed data asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                getInstance(context).bookshelfDao().apply {
                    val fictionId = insertCategory(Category(name = "Fiction"))
                    val fantasyId = insertCategory(Category(name = "Fantasy"))

                    val book1Id = insertBook(Book(title = "1984", author = "George Orwell", pages = 150, rating = 100))
                    val book2Id = insertBook(Book(title = "The Hobbit", author = "J.R.R. Tolkien", pages = 200, rating = 95))

                    insertBookCategoryCrossRef(
                        BookCategoryCrossRef(bookId = book1Id, categoryId = fictionId)
                    )
                    insertBookCategoryCrossRef(
                        BookCategoryCrossRef(bookId = book2Id, categoryId = fantasyId)
                    )
                }
            }
        }
    }
}