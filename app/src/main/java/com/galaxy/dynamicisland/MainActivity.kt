package com.galaxy.dynamicisland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.screens.CalibrationScreen
import com.galaxy.dynamicisland.ui.screens.HomeScreen
import com.galaxy.dynamicisland.ui.screens.LiveActivitiesScreen
import com.galaxy.dynamicisland.ui.screens.StayingAliveScreen
import com.galaxy.dynamicisland.ui.theme.DarkBorder
import com.galaxy.dynamicisland.ui.theme.DarkSurface
import com.galaxy.dynamicisland.ui.theme.GalaxyIslandTheme
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite

class MainActivity : ComponentActivity() {

    private lateinit var preferences: IslandPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = IslandPreferences(this)

        setContent {
            GalaxyIslandTheme {
                MainScreenContainer(preferences = preferences)
            }
        }
    }
}

@Composable
fun MainScreenContainer(preferences: IslandPreferences) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = PureBlack,
                contentColor = TextWhite
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Text("🏠", fontSize = 18.sp) },
                    label = { Text("Beranda", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SamsungSuperFastCyan,
                        selectedTextColor = SamsungSuperFastCyan,
                        indicatorColor = DarkBorder,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Text("📐", fontSize = 18.sp) },
                    label = { Text("Kalibrasi", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SamsungSuperFastCyan,
                        selectedTextColor = SamsungSuperFastCyan,
                        indicatorColor = DarkBorder,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Text("⚡", fontSize = 18.sp) },
                    label = { Text("Fitur S24U", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SamsungSuperFastCyan,
                        selectedTextColor = SamsungSuperFastCyan,
                        indicatorColor = DarkBorder,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Text("🛡️", fontSize = 18.sp) },
                    label = { Text("Anti-Kill", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SamsungSuperFastCyan,
                        selectedTextColor = SamsungSuperFastCyan,
                        indicatorColor = DarkBorder,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkSurface)
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
