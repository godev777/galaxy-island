package com.galaxy.dynamicisland.data

enum class IdleWidgetType(val title: String, val description: String) {
    CLOCK("Digital Clock", "Shows current time with on-the-hour tick"),
    WEATHER("Live Weather", "Temperature, rain chance, UV index (Open-Meteo)"),
    NET_SPEED("Network Speed", "Real-time download/upload speed meter"),
    BATTERY("Battery Status", "Percentage and charge state"),
    CALENDAR("Next Event", "Upcoming calendar schedule"),
    DND_STATUS("Do Not Disturb", "Current sound/vibrate profile indicator")
}

enum class ClockStyle {
    SMOOTH,
    LED_MATRIX_RED,
    LED_MATRIX_AMBER,
    LED_MATRIX_GREEN,
    LED_MATRIX_WHITE
}
