package org.k.flags

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.k.flags.country.Country
import org.k.flags.country.FactbookCountry
import org.k.flags.country.cleanFactbookText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import coil3.compose.AsyncImage
import org.k.flags.country.Flags

@Composable
fun CountrySummaryCard(country: Country, factbook: FactbookCountry?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header with Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Country Insights",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Basic Local Data (Overview)
            InsightSection(
                icon = Icons.Default.Public,
                label = "General Information",
                content = "Region: ${country.region}",
                flags = country.flags,
            )

            // 2. Factbook Data
            if (factbook != null) {
                // Background
                factbook.introduction?.background?.text?.let { bg ->
                    InsightSection(
                        icon = Icons.Default.Info,
                        label = "Background",
                        content = bg.cleanFactbookText()
                    )
                }

                // 3. Geography
                val geoContent = buildAnnotatedString {
                    factbook.geography?.location?.text?.let {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Location: ")
                        }
                        append("${it.cleanFactbookText()}\n")
                    }

                    factbook.geography?.terrain?.text?.let {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Terrain: ")
                        }
                        append("${it.cleanFactbookText()}\n")
                    }

                    factbook.geography?.climate?.text?.let {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Climate: ")
                        }
                        append(it.cleanFactbookText())
                    }
                }

                if (geoContent.isNotEmpty()) {
                    InsightSection(
                        icon = Icons.Default.Place,
                        label = "Geography",
                        annotatedContent = geoContent
                    )
                }

                // 4. Economy
                factbook.economy?.overview?.text?.let { overview ->
                    InsightSection(
                        icon = Icons.Default.ShoppingCart,
                        label = "Economic Overview",
                        content = overview.cleanFactbookText()
                    )
                }

                // 5. People & Society
                val peopleContent = buildAnnotatedString {
                    factbook.peopleAndSociety?.population?.total?.text?.let {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Population: ")
                        }
                        append("${it.cleanFactbookText()}\n")
                    }

                    factbook.peopleAndSociety?.religions?.text?.let {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Religions: ")
                        }
                        append(it.cleanFactbookText())
                    }
                }

                if (peopleContent.isNotEmpty()) {
                    InsightSection(
                        icon = Icons.Default.Groups,
                        label = "People & Society",
                        annotatedContent = peopleContent
                    )
                }

                // 6. Government (Time Difference)
                factbook.government?.capital?.timeDifference?.text?.let { time ->
                    InsightSection(
                        icon = Icons.Default.Schedule,
                        label = "Time Difference",
                        content = time.cleanFactbookText()
                    )
                }

            } else if (country.factbookUrl?.isNotBlank() == true) {
                // Loading State
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightSection(
    icon: ImageVector,
    label: String,
    content: String? = null,
    annotatedContent: AnnotatedString? = null,
    flags: Flags? = null
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (flags != null) {
                AsyncImage(
                    model = flags.png,
                    contentDescription = flags.alt,
                    modifier = Modifier
                        .size(width = 28.dp, height = 20.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Display whichever content is provided
        Text(
            text = annotatedContent ?: AnnotatedString(content ?: ""),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
