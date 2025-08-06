package com.maherlabbad.hayattakal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.maherlabbad.hayattakal.Screens.MainScreen
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.Relative_model

import com.maherlabbad.hayattakal.ui.theme.HayattaKalTheme
import com.maherlabbad.hayattakal.viewmodel.Relative_Model_viewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HayattaKalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notify_Relatives_Screen(items : List<Relative_model>){

    Scaffold(floatingActionButton = {FAB()}
        ,floatingActionButtonPosition = FabPosition.End
        ,topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Yakınlara Haber Ver",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Geri gitme işlemi */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {

            items(items.size){
                Relative_Row(relativeModel = items[it])
            }
        }
    }
}

@Composable
fun FAB(){
    FloatingActionButton(onClick = { /* TODO: Yeni bir kişi Ekleme */ }, shape = RoundedCornerShape(16.dp), containerColor = MaterialTheme.colorScheme.onSurface) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Yeni Kişi Ekle", tint = Color.White)
    }
}


@Composable
fun Relative_Row(relativeModel: Relative_model){
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row {
            Column(modifier = Modifier.weight(1f).padding(8.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = relativeModel.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = relativeModel.phone_number, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(modifier = Modifier.padding(8.dp),
                onClick = { /* TODO: Yakınlara haber gönderme işlemi */ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "Mesaj Gönder")
            }
        }
    }
}


@Composable
fun ContactPicker(){

    val context = LocalContext.current

    val contactpickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri ->
        uri?.let {
            val projection = arrayOf(
                android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER,
                android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val cursor = context.contentResolver.query(it, projection, null, null, null)?.use { it

                    if(it.moveToFirst()){
                        val numberIndex = it.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex = it.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                        val name = it.getString(nameIndex)
                        val number = it.getString(numberIndex)
                        val relativeModel = Relative_model(name,number)



                    }
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HayattaKalTheme {
        Notify_Relatives_Screen(listOf(Relative_model("maher","4848445584"),Relative_model("maher","4848445584")))
    }
}