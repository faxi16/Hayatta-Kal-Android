package com.maherlabbad.hayattakal.Screens

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.maherlabbad.hayattakal.model.Relative_model
import com.maherlabbad.hayattakal.viewmodel.RelativeModelviewmodel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(navController: NavController,relativeModelviewmodel: RelativeModelviewmodel) {

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions =
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS
        ))

    LaunchedEffect(Unit) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    Scaffold(floatingActionButton = {
        val contacts = relativeModelviewmodel.itemList.value
        FABEmergency(contacts = contacts)
                                    }
        ,floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ana Sayfa",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                    icon = Icons.Outlined.Info,
                    text = "İlk Yardım Bilgileri",
                    onClick = { navController.navigate("First_Aid_Screen") }
                )
            }

            item {
                FeatureCard(
                    icon = Icons.Outlined.Menu,
                    text = "Afet Çantası",
                    onClick = { navController.navigate("Disaster_Bag_Screen") }
                )
            }
            item {
                FeatureCard(
                    icon = Icons.Outlined.Send,
                    text = "Yakınlara Haber Ver",
                    onClick = { navController.navigate("Notify_Relatives_Screen") }
                )
            }
            item {
                FeatureCard(
                    icon = Icons.Rounded.Search,
                    text = "Anlık Deprem Takibi",
                    onClick = { navController.navigate("Earthquake_Screen") }
                )
            }
        }


    }
}

@Composable
fun FABEmergency(contacts : List<Relative_model>){
    val context = LocalContext.current
    FloatingActionButton(onClick = {
        SendSmstoAll(context,contacts)
    }, shape = RoundedCornerShape(16.dp), containerColor = Color.Red) {
        Column {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Acil Yardım",
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text("Tehlikedeyim", color = Color.White, modifier = Modifier.padding(4.dp).align(Alignment.CenterHorizontally))

        }
    }
}

fun SendSmstoAll(context: Context,contacts : List<Relative_model>){

    val smsIntent = Intent(Intent.ACTION_VIEW).apply {
        val numbers = contacts.joinToString(separator = ",") { it.phone_number }
        data = "sms:${numbers}".toUri()
        putExtra("sms_body", "Tehlikedeyim!!!")
    }
    context.startActivity(smsIntent)
}


@Composable
fun FeatureCard(icon: ImageVector, text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}