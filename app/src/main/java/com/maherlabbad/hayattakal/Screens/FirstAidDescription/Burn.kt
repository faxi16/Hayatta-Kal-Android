package com.maherlabbad.hayattakal.Screens.FirstAidDescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Burn_screen(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Yanıklar",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Geri gitme işlemi */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // "NE YAPILMALI?" Bölümü
            InstructionSection(
                title = "NE YAPILMALI?",
                backgroundColor = Color(0xFF4CAF50),
                instructions = listOf(
                    "Soğutun: Yanan bölgeyi en az 15-20 dakika boyunca ağrıyı azaltacak kadar soğuk (buzlu olmayan) akan suyun altında tutun.",

                    "Takıları Çıkarın: Bölge şişmeden önce yüzük, bilezik, saat gibi takıları çıkarın.",

                    "Temiz Bir Bezle Kapatın: Yanan bölgenin üzerini temiz ve tüy bırakmayan nemli bir bezle veya streç film ile gevşekçe kapatın.",

                    "Şoka Karşı Önlem Alın: Yanık geniş bir alandaysa, yaralının üzerini örterek vücut ısısını koruyun."
                ),
                isPositive = true
            )


            InstructionSection(
                title = "NELERDEN KAÇINILMALI?",
                backgroundColor = Color(0xFFF44336),
                instructions = listOf(
                    "Yanığın üzerine diş macunu, yoğurt, salça, zeytinyağı gibi maddeler sürmeyin.",

                    "Oluşan su kabarcıklarını (bülleri) patlatmayın.",

                    "Yanıcının üzerine yapışmış giysileri çekerek çıkarmaya çalışmayın.",

                    "Buz veya buzlu su kullanmayın, bu daha fazla doku hasarına yol açabilir."

                ),
                isPositive = false
            )
        }
    }
}