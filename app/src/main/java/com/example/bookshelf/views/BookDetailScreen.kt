package com.example.bookshelf.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.bookshelf.data.*


@Composable
fun BookDetailScreen(
    book: Book,
    onEditBookClick: (Book) -> Unit,
    onDeleteBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Autor: ${book.author}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Počet stran: ${book.pages}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        book.description?.let {
            Text(
                text = "Popis: $it",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = "Hodnocení: ${book.rating}/5",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Tlačítko na úpravu knihy
            Button(
                onClick = { onEditBookClick(book) },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Upravit")
            }

            // Tlačítko na smazání knihy
            Button(
                onClick = { onDeleteBookClick(book) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(text = "Smazat")
            }
        }
    }
}
