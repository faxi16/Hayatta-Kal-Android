package com.maherlabbad.hayattakal.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstAidScreen(){


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "İlk Yardım Bilgileri",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ), modifier = Modifier.padding(top = 20.dp)
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FeatureCard(
                    icon = Icons.TwoTone.Info,
                    text = "Kırık ve Çıkıklar",
                    onClick = { /* TODO: İlk Yardım ekranına yönlendirme */ }
                )
            }
            item {
                FeatureCard(
                    icon = Icons.TwoTone.Info,
                    text = "Yanıklar",
                    onClick = { /* TODO: Acil Numaralar ekranına yönlendirme */ }
                )
            }
            item {
                FeatureCard(
                    icon = Icons.TwoTone.Info,
                    text = "kanamalar",
                    onClick = { /* TODO: Afet Çantası ekranına yönlendirme */ }
                )
            }
            item {
                FeatureCard(
                    icon = Icons.TwoTone.Info,
                    text = "Ezilme",
                    onClick = { /* TODO: Yakınlara Haber Ver ekranına yönlendirme */ }
                )
            }

        }
    }
}