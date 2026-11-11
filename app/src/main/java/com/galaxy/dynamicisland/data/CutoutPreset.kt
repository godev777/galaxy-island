package com.galaxy.dynamicisland.data

enum class CutoutPosition {
    CENTER,
    TOP_RIGHT,
    TOP_LEFT,
    CUSTOM
}

data class CutoutPreset(
    val name: String,
    val description: String,
    val position: CutoutPosition,
    val offsetX: Int,        // dp offset from gravity anchor
    val offsetY: Int,        // dp margin from top of screen
    val collapsedWidth: Int, // dp
    val collapsedHeight: Int,// dp
    val cornerRadius: Int    // dp
) {
    companion object {
        // Dedicated, pixel-perfect preset for Samsung Galaxy S24 Ultra
        val S24_ULTRA = CutoutPreset(
            name = "Galaxy S24 Ultra (Recommended)",
            description = "Center punch-hole, 3.5mm camera cutout, 120Hz LTPO",
            position = CutoutPosition.CENTER,
            offsetX = 0,
            offsetY = 11,
            collapsedWidth = 116,
            collapsedHeight = 34,
            cornerRadius = 17
        )

        val S23_S22_S21 = CutoutPreset(
            name = "Galaxy S21 / S22 / S23 Series",
            description = "Center punch hole, compact pill",
            position = CutoutPosition.CENTER,
            offsetX = 0,
            offsetY = 10,
            collapsedWidth = 110,
            collapsedHeight = 34,
            cornerRadius = 17
        )

        val GALAXY_A_SERIES = CutoutPreset(
            name = "Galaxy A Series (A54, A55, A34)",
            description = "Infinity-O cutout with slightly wider ring",
            position = CutoutPosition.CENTER,
            offsetX = 0,
            offsetY = 12,
            collapsedWidth = 120,
            collapsedHeight = 36,
            cornerRadius = 18
        )

        val S10_PLUS_CORNER = CutoutPreset(
            name = "Galaxy S10 / S10+ (Corner Cutout)",
            description = "Top right corner punch-hole",
            position = CutoutPosition.TOP_RIGHT,
            offsetX = 24,
            offsetY = 12,
            collapsedWidth = 120,
            collapsedHeight = 36,
            cornerRadius = 18
        )

        val presets = listOf(S24_ULTRA, S23_S22_S21, GALAXY_A_SERIES, S10_PLUS_CORNER)
    }
}
