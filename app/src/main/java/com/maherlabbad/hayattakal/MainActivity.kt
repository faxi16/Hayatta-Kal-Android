package com.maherlabbad.hayattakal

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

import com.maherlabbad.hayattakal.Screens.MainScreen
import com.maherlabbad.hayattakal.Screens.Notify_Relatives_Screen
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.model.KandilliEarthquake
import com.maherlabbad.hayattakal.model.Relative_model

import com.maherlabbad.hayattakal.ui.theme.HayattaKalTheme
import com.maherlabbad.hayattakal.viewmodel.EarthquakeViewModel
import com.maherlabbad.hayattakal.viewmodel.RelativeModelviewmodel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel : RelativeModelviewmodel by viewModels<RelativeModelviewmodel>()
    private val viewModel2 : EarthquakeViewModel by viewModels<EarthquakeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HayattaKalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {

                        Earthquake_screen()

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Earthquake_screen(){


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Deprem Takibi",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { /* Geri gitme işlemi */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Veri Kaynağını Değiştir"
                        )
                    }
                }
            )
        }
    ) {innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                EarthquakeRow(Earthquake = EarthquakeModel("1","154544544","1","1","1","5","1","ghfjskkcksakl"))
            }
        }

    }
}


@Composable
fun EarthquakeRow(Earthquake: EarthquakeModel){

    val Magnitude = Earthquake.magnitude.toDouble()
    var color = Color.Black
    if(Magnitude < 5 && Magnitude > 3.5){
        color = Color.Green
    }else if(Magnitude > 4.5){
        color = Color.Red
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {  }),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Row(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {

                Text(Earthquake.location)
                Text(Earthquake.date)
            }

            Text(Earthquake.magnitude, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)

        }
    }
}
fun getStartAndEndDatesForLast24Hours(): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val calendar = Calendar.getInstance()
    val endDate = dateFormat.format(calendar.time)

    calendar.add(Calendar.HOUR_OF_DAY, -24)
    val startDate = dateFormat.format(calendar.time)

    return Pair(startDate, endDate)
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HayattaKalTheme {
        Earthquake_screen()
    }
}