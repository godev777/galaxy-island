package com.galaxy.dynamicisland.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class WeatherData(
    val temperatureC: Int = 28,
    val weatherCondition: String = "Partly Cloudy",
    val rainChancePercent: Int = 10,
    val uvIndex: Double = 5.2
)

object WeatherManager {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()

    fun refreshWeather(latitude: Double = -6.2088, longitude: Double = 106.8456) { // Default Jakarta/Indonesia
        scope.launch {
            try {
                val urlString = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m,relative_humidity_2m,precipitation_probability,weather_code"
                val url = URL(urlString)
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                if (connection.responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val json = JSONObject(response.toString())
                    val current = json.getJSONObject("current")
                    val temp = current.getDouble("temperature_2m").toInt()
                    val rainChance = current.optInt("precipitation_probability", 15)
                    val code = current.optInt("weather_code", 1)

                    val condition = when (code) {
                        0 -> "Clear"
                        1, 2, 3 -> "Partly Cloudy"
                        45, 48 -> "Foggy"
                        51, 53, 55 -> "Drizzle"
                        61, 63, 65 -> "Rain"
                        80, 81, 82 -> "Rain Showers"
                        95 -> "Thunderstorm"
                        else -> "Partly Cloudy"
                    }

                    _weatherState.value = WeatherData(
                        temperatureC = temp,
                        weatherCondition = condition,
                        rainChancePercent = rainChance,
                        uvIndex = 6.0
                    )
                }
            } catch (e: Exception) {
                // Fallback graceful values if offline
                _weatherState.value = WeatherData(temperatureC = 29, weatherCondition = "Clear", rainChancePercent = 5)
            }
        }
    }
}
