package com.example.bookshelf.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.bookshelf.data.*


@Composable
fun BookDetailScreen(
    book: BookWithCategories,
    onEditBookClick: (BookWithCategories) -> Unit,
    onDeleteBookClick: (BookWithCategories) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = book.book.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight(800))){
                    append("Author: ")
                }
                append(book.book.author)
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight(800))){
                    append("Number of pages: ")
                }
                append(book.book.pages.toString())
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        book.book.description?.let {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight(800))){
                        append("Description: ")
                    }
                    append(it)
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight(800))){
                    append("Rating: ")
                }
                append(book.book.rating.toString())
                append("/5")
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight(800))){
                    append("Categories: ")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(book.categories){ category ->
                Text(
                    text = category.name,
                    modifier = Modifier.background(MaterialTheme.colorScheme.inversePrimary, CircleShape).padding(10.dp)
                )
            }
            if (book.categories.isEmpty()){
                item {
                    Text("None")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onEditBookClick(book) },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(text = "Edit")
            }

            Button(
                onClick = { onDeleteBookClick(book) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(text = "Delete")
            }
        }
    }
}
