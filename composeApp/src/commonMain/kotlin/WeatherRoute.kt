import kotlinx.serialization.Serializable

@Serializable
data class WeatherRoute(
    val cityName: String,
    val lat: Float,
    val long: Float
)