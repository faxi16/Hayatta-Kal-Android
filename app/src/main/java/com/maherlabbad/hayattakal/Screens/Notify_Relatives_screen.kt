package com.maherlabbad.hayattakal.Screens


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.provider.ContactsContract

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi

import com.google.accompanist.permissions.rememberPermissionState
import com.maherlabbad.hayattakal.model.Relative_model
import com.maherlabbad.hayattakal.viewmodel.RelativeModelviewmodel

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Notify_Relatives_Screen(Relative_Model_viewmodel: RelativeModelviewmodel){

    val context = LocalContext.current

    val contacts = Relative_Model_viewmodel.itemList.value

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let {
            val cursor = context.contentResolver.query(it, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val hasPhoneNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                val contactId = cursor.getString(idIndex)
                val name = cursor.getString(nameIndex)
                val hasPhoneNumber = cursor.getInt(hasPhoneNumberIndex)

                if (hasPhoneNumber > 0) {
                    val phones = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(contactId),
                        null
                    )

                    phones?.use {
                        if (it.moveToFirst()) {
                            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val number = it.getString(numberIndex)

                            val relativeModel = Relative_model(name, number)
                            Relative_Model_viewmodel.saveItem(relativeModel)
                        }
                    }
                }

            }
            cursor?.close()
        }
    }

    Scaffold(floatingActionButton = {FAB(contactPickerLauncher)}
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
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

            items(contacts.size){
                Relative_Row(relativeModel = contacts[it], Relative_Model_viewmodel = Relative_Model_viewmodel)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FAB(contactpickerLauncher : ActivityResultLauncher<Void?>){
    val context = LocalContext.current
    val permission = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    FloatingActionButton(onClick = {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            permission.launchPermissionRequest()
        }else{
            contactpickerLauncher.launch(null)
        }

    }, shape = RoundedCornerShape(16.dp), containerColor = MaterialTheme.colorScheme.onSurface) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Yeni Kişi Ekle", tint = Color.White)
    }
}

@Composable
fun Relative_Row(relativeModel: Relative_model,Relative_Model_viewmodel: RelativeModelviewmodel){
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row {
            Column(modifier = Modifier.weight(1f).padding(8.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = relativeModel.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.clickable(onClick = {
                        Relative_Model_viewmodel.deleteItem(relativeModel)
                    })
                )
                Text(text = relativeModel.phone_number, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(modifier = Modifier.padding(8.dp),
                onClick = {
                    SendSms(context = context, relativeModel = relativeModel)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "Mesaj Gönder")
            }
        }
    }
}

fun SendSms(context : Context, relativeModel: Relative_model){
    val smsIntent = Intent(Intent.ACTION_VIEW).apply {
        data = "sms:${relativeModel.phone_number}".toUri()
        putExtra("sms_body", "message")
    }
    context.startActivity(smsIntent)
}