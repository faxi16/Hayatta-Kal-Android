package com.maherlabbad.hayattakal.Screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ChecklistItem(
    val text: String,
    var isChecked: Boolean = false
)

fun getInitialChecklist(): SnapshotStateList<ChecklistItem> {
    return mutableStateListOf(
        ChecklistItem("Su (kişi başı en az 2 litre)"),
        ChecklistItem("Hazır gıda (konserve, kuruyemiş vs.)"),
        ChecklistItem("İlk yardım malzemeleri"),
        ChecklistItem("El feneri ve yedek piller"),
        ChecklistItem("Powerbank (şarjlı)"),
        ChecklistItem("Battaniye / Termal battaniye"),
        ChecklistItem("Önemli belge fotokopileri (nüfus cüzdanı, ruhsat vb.)"),
        ChecklistItem("Düdük"),
        ChecklistItem("Maske, eldiven, dezenfektan")
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterBagScreen(navController: NavController) {
    val checklistItems = remember { getInitialChecklist() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Afet Çantası", fontWeight = FontWeight.Bold) }
                ,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("MainScreen")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Her bireyin afet çantasını önceden hazırlaması hayati önem taşır. Çantanızda aşağıdaki temel malzemeleri bulundurun:",
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )


            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(checklistItems) { index, item ->
                    ChecklistItemRow(
                        item = item,
                        onCheckedChange = { newItemState ->
                            checklistItems[index] = item.copy(isChecked = newItemState)
                        }
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }


            BottomActionsRow(
                onCheckAll = {
                    checklistItems.forEachIndexed { index, item ->
                        checklistItems[index] = item.copy(isChecked = true)
                    }
                },
                onClearAll = {
                    checklistItems.forEachIndexed { index, item ->
                        checklistItems[index] = item.copy(isChecked = false)
                    }
                },
                onShare = {
                    shareChecklist(context, checklistItems)
                }
            )
        }
    }
}


@Composable
fun ChecklistItemRow(
    item: ChecklistItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!item.isChecked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.text,
            fontSize = 17.sp
        )
    }
}


@Composable
fun BottomActionsRow(
    onCheckAll: () -> Unit,
    onClearAll: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onCheckAll) {
            Text("Tümünü İşaretle", fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onClearAll) {
            Text("Temizle", fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onShare) {
            Text("Paylaş", fontWeight = FontWeight.Bold)
        }
    }
}


fun shareChecklist(context: Context, items: List<ChecklistItem>) {
    val shareText = buildString {
        append("Afet Çantası Hazırlık Listem:\n\n")
        items.forEach { item ->
            val status = if (item.isChecked) "[✓]" else "[ ]"
            append("$status ${item.text}\n")
        }
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Afet Çantası Listesi")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, "Listeyi Paylaş"))
}