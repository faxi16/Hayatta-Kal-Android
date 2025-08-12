package com.maherlabbad.hayattakal

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import com.maherlabbad.hayattakal.Screens.DisasterBagScreen
import com.maherlabbad.hayattakal.Screens.Earthquake_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Bleeding_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Burn_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.Crush_Syndrome_screen
import com.maherlabbad.hayattakal.Screens.FirstAidDescription.FracturesDislocations
import com.maherlabbad.hayattakal.Screens.FirstAidScreen
import com.maherlabbad.hayattakal.Screens.MainScreen
import com.maherlabbad.hayattakal.Screens.MapScreen
import com.maherlabbad.hayattakal.Screens.NewsScreen
import com.maherlabbad.hayattakal.Screens.Notify_Relatives_Screen
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.ui.theme.HayattaKalTheme
import com.maherlabbad.hayattakal.viewmodel.EarthquakeViewModel
import com.maherlabbad.hayattakal.viewmodel.NewsViewModel
import com.maherlabbad.hayattakal.viewmodel.RelativeModelviewmodel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val relativeModelviewmodel : RelativeModelviewmodel by viewModels<RelativeModelviewmodel>()

    private val newsViewModel : NewsViewModel by viewModels<NewsViewModel>()
    private val earthquakeViewModel : EarthquakeViewModel by viewModels<EarthquakeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            HayattaKalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = "MainScreen") {
                            composable("MainScreen") {
                                MainScreen(navController,relativeModelviewmodel)
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
                            composable("Disaster_Bag_Screen") {
                                DisasterBagScreen(navController)
                            }
                            composable("MapScreen/{encoded}"
                            , arguments =
                            listOf(navArgument("encoded") { type = NavType.StringType }))
                            { backStackEntry ->
                                val jsonEncoded = backStackEntry.arguments?.getString("encoded")
                                val json = Uri.decode(jsonEncoded)
                                val relative = Gson().fromJson(json, EarthquakeModel::class.java)
                                MapScreen(relative,navController)
                            }
                            composable("NewsScreen") {
                                NewsScreen(newsViewModel,navController)
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