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
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Crush_Syndrome_screen(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ezilme",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("First_Aid_Screen") }) {
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
                    "Profesyonel Yardım Bekleyin: Yaralıyı sıkıştığı yerden sadece eğitimli kurtarma ekipleri (AFAD, UMKE, İtfaiye) çıkarmalıdır.",
                    "Bilincini Açık Tutun: Yaralıyla sürekli konuşarak bilincini açık tutmaya ve moral vermeye çalışın.",
                    "Görünen Kanamaları Durdurun: Eğer yaralının ulaşabildiğiniz bir yerinde aktif kanama varsa, üzerine temiz bir bezle baskı uygulayın."
                ),
                isPositive = true
            )


            InstructionSection(
                title = "NELERDEN KAÇINILMALI?",
                backgroundColor = Color(0xFFF44336),
                instructions = listOf(
                    "AĞIRLIĞI ANİDEN KALDIRMAYIN: Uzun süre baskı altında kalmış kaslardan kana bir anda zehirli maddeler karışır. Bu, ani kalp durmasına ve böbrek yetmezliğine yol açabilir. Bu yüzden kurtarma işlemini profesyonellere bırakın.",

                    "HEMEN SU VERMEYİN: Kurtarıldıktan hemen sonra yaralıya su içirmek, böbreklerin iflas etmesine neden olabilir. Sıvı takviyesi sadece damar yoluyla ve sağlık ekipleri tarafından yapılmalıdır."
                ),
                isPositive = false
            )
        }
    }
}