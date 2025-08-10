package com.maherlabbad.hayattakal.Screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.viewmodel.EarthquakeViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Earthquake_screen(Earthquakeviewmodel : EarthquakeViewModel,navController: NavController){
    val earthquakes by Earthquakeviewmodel.earthquakes.collectAsState()
    val currentSource by Earthquakeviewmodel.dataSource.collectAsState()
    val isLoading by Earthquakeviewmodel.isLoading.collectAsState()
    val context = LocalContext.current

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
                    IconButton(onClick = {
                        navController.navigate("MainScreen")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },actions = {
                    IconButton(
                        onClick = {
                            Earthquakeviewmodel.toggleDataSource()
                            val nextSource = if (currentSource == "AFAD") "Kandilli" else "AFAD"
                            Toast.makeText(context, "$nextSource verileri yükleniyor...", Toast.LENGTH_SHORT).show()
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

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = earthquakes) { earthquake ->
                    EarthquakeRow(Earthquake = earthquake,CurrentSource = currentSource, navController = navController)
                }
            }

        }

    }
}

@Composable
fun EarthquakeRow(Earthquake: EarthquakeModel,CurrentSource : String,navController: NavController){

    val Magnitude = Earthquake.magnitude.toDouble()
    var color = Color.Black
    if(Magnitude < 5 && Magnitude > 3.5){
        color = Color.Green
    }else if(Magnitude > 4.5){
        color = Color.Red
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            val gson = Gson()
            val json = gson.toJson(Earthquake)
            val encoded = Uri.encode(json)
            navController.navigate("MapScreen/$encoded")
        }),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Row(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {

                Text(Earthquake.location)
                if(CurrentSource == "AFAD"){
                    Text(convertUtcToTurkeyTime(Earthquake.date))
                }else{
                    Text(Earthquake.date)
                }
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

fun convertUtcToTurkeyTime(utcTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val utcDateTime = LocalDateTime.parse(utcTimeString, formatter)

    val utcZoned = ZonedDateTime.of(utcDateTime, ZoneId.of("UTC"))
    val turkeyZoned = utcZoned.withZoneSameInstant(ZoneId.of("Europe/Istanbul"))

    return turkeyZoned.format(formatter)
}