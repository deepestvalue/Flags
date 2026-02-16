package org.k.flags

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.k.flags.country.CapitalInfo

@Composable
fun WeatherButton(
    modifier: Modifier = Modifier,
    country: String,
    capitals: List<String>,
    capitalInfo: CapitalInfo,
    navigateToWeather: (String, String, Double, Double) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            capitals.forEach {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    ),
                    onClick = {
                        navigateToWeather(
                            country,
                            it,
                            capitalInfo.latlng[0],
                            capitalInfo.latlng[1]
                        )
                    }) {
                    Text(text = "$it weather")
                }
            }
        }
    }
}