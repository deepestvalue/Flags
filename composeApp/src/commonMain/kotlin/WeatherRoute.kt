import kotlinx.serialization.Serializable

@Serializable
data class WeatherRoute(
    val countryOfficialName: String,
    // if more than one capital, the first one is selected
    val capitalName: String,
    val capitalLat: Float,
    val capitalLong: Float
)