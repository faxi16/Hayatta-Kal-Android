package com.maherlabbad.hayattakal.Screens.FirstAidDescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bleeding_screen(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Kanamalar",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("First_Aid_Screen") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                InstructionSection(
                    title = "NE YAPILMALI?",
                    backgroundColor = Color(0xFF4CAF50),
                    instructions = listOf(
                        "Doğrudan Baskı Uygula: Temiz bir bezle kanayan yere sıkıca bastır.",
                        "Bezi Kaldırma: Kanla dolsa bile ilk bezi kaldırma, üzerine yenisini ekle.",

                        "Yüksekte Tut: Kırık yoksa kanayan kolu veya bacağı kalp seviyesinden yukarı kaldır.",

                        "Şok Pozisyonu Ver: Yaralıyı sırt üstü yatır, bacaklarını 30 cm yükselt ve üzerini ört.",

                        "Turnike Uygula (Son Çare): Sadece durdurulamayan ve hayatı tehdit eden kanamalarda, eğitimliysen uygula."
                    ),
                    isPositive = true
                )
            }

            item {
                InstructionSection(
                    title = "NELERDEN KAÇINILMALI?",
                    backgroundColor = Color(0xFFF44336),
                    instructions = listOf(
                        "Yaranın içindeki cisimleri çıkarmayın",
                        "Kanla ıslanan ilk bezi kaldırmayın",
                        "Turnikeyi son çare olmadan uygulamayın"
                    ),
                    isPositive = false
                )
            }
        }
    }
}