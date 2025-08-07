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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maherlabbad.hayattakal.Screens.Earthquake_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Bleeding_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Burn_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Crush_Syndrome_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.FracturesDislocations
import com.maherlabbad.hayattakal.Screens.FirstAidScreen

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
    private val relativeModelviewmodel : RelativeModelviewmodel by viewModels<RelativeModelviewmodel>()

    private val earthquakeViewModel : EarthquakeViewModel by viewModels<EarthquakeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            HayattaKalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = "MainScreen") {
                            composable("MainScreen") {
                                MainScreen(navController)
                            }
                            composable("Notify_Relatives_Screen") {
                                Notify_Relatives_Screen(relativeModelviewmodel, navController = navController)
                            }
                            composable("Earthquake_Screen") {
                                Earthquake_screen(earthquakeViewModel, navController = navController)
                            }
                            composable("Fractures_dislocation_Screen") {
                                FracturesDislocations(navController)
                            }
                            composable("Burn_Screen") {
                                Burn_screen(navController)
                            }
                            composable("Bleeding_Screen") {
                                Bleeding_screen(navController)
                            }
                            composable("CrushSyndrome_Screen") {
                                Crush_Syndrome_screen(navController)
                            }
                            composable("First_Aid_Screen") {
                                FirstAidScreen(navController)
                            }


                        }

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

    }
}