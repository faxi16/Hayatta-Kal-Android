package com.maherlabbad.hayattakal

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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.maherlabbad.hayattakal.Screens.MainScreen
import com.maherlabbad.hayattakal.Screens.Notify_Relatives_Screen
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.Relative_model

import com.maherlabbad.hayattakal.ui.theme.HayattaKalTheme
import com.maherlabbad.hayattakal.viewmodel.RelativeModelviewmodel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel : RelativeModelviewmodel by viewModels<RelativeModelviewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HayattaKalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        viewModel.getItemList()

                        Notify_Relatives_Screen(Relative_Model_viewmodel = viewModel)
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