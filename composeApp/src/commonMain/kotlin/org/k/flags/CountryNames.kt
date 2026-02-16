package org.k.flags

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import org.k.flags.country.Name

@Composable
fun CountryNames(modifier: Modifier = Modifier, name: Name) {
    Column(modifier = modifier) {
        Text(text = name.common, style = MaterialTheme.typography.bodyLarge)
        Text(text = name.official, style = MaterialTheme.typography.bodyMedium)
    }
}