package org.k.flags

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.k.flags.country.Flags

@Composable
fun Flag(modifier: Modifier = Modifier, flags: Flags) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = MaterialTheme.shapes.small
    ) {
        AsyncImage(
            model = flags.png,
            contentDescription = flags.alt,
            contentScale = ContentScale.FillBounds
        )
    }
}