package com.maherlabbad.hayattakal.Screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maherlabbad.hayattakal.SettingsManager


@Composable
fun SettingsAndAboutScreen(context: Context) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ayarlar", "Hakkında")

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> SettingsScreen(context)
            1 -> AboutScreen()
        }
    }
}

@Composable
fun SettingsScreen(context: Context) {
    val settingsManager = remember { SettingsManager(context) }
    var minMagnitudeNotification by remember {
        mutableStateOf(settingsManager.getFloat("min_magnitude_for_notification", 4.0f))
    }
    var maxDistance by remember {
        mutableStateOf(settingsManager.getInt("max_distance_in_kilometers", 100))
    }
    var minMagnitudeSMS by remember {
        mutableStateOf(settingsManager.getFloat("min_magnitude_for_sms", 5.0f))
    }
    var minMagnitudeNotificationInDistance by remember {
        mutableStateOf(settingsManager.getFloat("min_magnitude_for_notification_in_distance", 4.5f))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Uygulama Ayarları", style = MaterialTheme.typography.headlineSmall)

        SettingSlider(
            title = "Bildirim için minimum deprem şiddeti",
            value = minMagnitudeNotification,
            valueRange = 0f..10f,
            onValueChange = {
                minMagnitudeNotification = it
                settingsManager.setFloat("min_magnitude_for_notification", it)
            }
        )

        SettingSlider(
            title = "Konum tabanlı bildirim için maksimum mesafe (km)",
            value = maxDistance.toFloat(),
            valueRange = 0f..1000f,
            onValueChange = {
                maxDistance = it.toInt()
                settingsManager.setInt("max_distance_in_kilometers", it.toInt())
            }
        )

        SettingSlider(
            title = "Yakın kişilere SMS göndermek için minimum deprem şiddeti",
            value = minMagnitudeSMS,
            valueRange = 0f..10f,
            onValueChange = {
                minMagnitudeSMS = it
                settingsManager.setFloat("min_magnitude_for_sms", it)
            }
        )

        SettingSlider(
            title = "Belirli mesafe içinde bildirim için minimum şiddet",
            value = minMagnitudeNotificationInDistance,
            valueRange = 0f..10f,
            onValueChange = {
                minMagnitudeNotificationInDistance = it
                settingsManager.setFloat("min_magnitude_for_notification_in_distance", it)
            }
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun SettingSlider(title: String, value: Float, valueRange: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column {
        Text("$title: ${String.format("%.1f", value)}")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Uygulama Hakkında", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Hayatta Kal uygulaması, deprem anlarında sizi bilgilendirmek ve güvenliğinizi artırmak amacıyla geliştirilmiştir. " +
                    "Bu uygulama, AFAD verilerini kullanarak deprem bildirimleri, SMS uyarıları ve konum tabanlı bilgilendirmeler sağlar.\n\n" +
                    "Versiyon: 1.0\nGeliştirici: Maher Labbad",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}