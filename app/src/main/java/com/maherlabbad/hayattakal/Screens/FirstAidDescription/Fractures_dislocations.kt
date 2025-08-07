package com.maherlabbad.hayattakal.Screens.FirstAidDescription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle // 'X' ikonu için daha uygun
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FracturesDislocations(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Kırık ve Çıkıklar",
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
                    backgroundColor = Color(0xFF4CAF50), // Green
                    instructions = listOf(
                        "Hareket Ettirme: Kırık şüphesi olan bölgeyi ve yaralıyı kesinlikle oynatma.",
                        "Sabitle (Tespit Et): Kırık bölgeyi, alt ve üst eklemleri de içine alacak şekilde sert bir cisimle (atel) hareketsiz hale getir. ",
                        "Açık Kırığı Kapat: Kırık kemik dışarı çıktıysa, üzerini temiz bir bezle ört. Asla içeri itme.",
                        "Buz Uygula: Şişliği azaltmak için bölgeye bir beze sarılı buz torbasıyla kompres yap."
                    ),
                    isPositive = true
                )
            }
            item {
                InstructionSection(
                    title = "NELERDEN KAÇINILMALI?",
                    backgroundColor = Color(0xFFF44336), // Red
                    instructions = listOf(
                        "Düzeltmeye Çalışma: Kırığı veya çıkığı asla yerine oturtmaya çalışma.",
                        "İlaç Verme: Doktor onayı olmadan ağrı kesici dahil hiçbir ilaç verme.",
                        "Masaj Yapma: Kırık bölgeye kesinlikle masaj yapma."
                    ),
                    isPositive = false
                )
            }
        }
    }
}

@Composable
fun InstructionSection(
    title: String,
    backgroundColor: Color,
    instructions: List<String>,
    isPositive: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            instructions.forEach { text ->
                InstructionRow(
                    text = text,
                    isPositive = isPositive
                )
            }
        }
    }
}

@Composable
fun InstructionRow(text: String, isPositive: Boolean) {
    val icon = if (isPositive) Icons.Default.CheckCircle else Icons.Default.Clear

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
