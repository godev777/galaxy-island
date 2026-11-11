package com.galaxy.dynamicisland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.AppLanguage
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.screens.CalibrationScreen
import com.galaxy.dynamicisland.ui.screens.HomeScreen
import com.galaxy.dynamicisland.ui.screens.LiveActivitiesScreen
import com.galaxy.dynamicisland.ui.screens.StayingAliveScreen
import com.galaxy.dynamicisland.ui.theme.GalaxyIslandTheme

class MainActivity : ComponentActivity() {

    private lateinit var preferences: IslandPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = IslandPreferences(this)

        setContent {
            val themeMode by preferences.themeMode.collectAsState()

            GalaxyIslandTheme(themeMode = themeMode) {
                MainScreenContainer(preferences = preferences)
            }
        }
    }
}

@Composable
fun MainScreenContainer(preferences: IslandPreferences) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val appLang by preferences.appLanguage.collectAsState()
    val isIndo = appLang == AppLanguage.INDONESIAN

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Text("🏠", fontSize = 18.sp) },
                    label = { Text(if (isIndo) "Beranda" else "Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Text("📐", fontSize = 18.sp) },
                    label = { Text(if (isIndo) "Kalibrasi" else "Calibrate", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Text("⚡", fontSize = 18.sp) },
                    label = { Text(if (isIndo) "Fitur S24U" else "Features", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Text("🛡️", fontSize = 18.sp) },
                    label = { Text(if (isIndo) "Anti-Kill" else "Guide", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(preferences = preferences)
                1 -> CalibrationScreen(preferences = preferences)
                2 -> LiveActivitiesScreen(preferences = preferences)
                3 -> StayingAliveScreen()
            }
        }
    }
}
