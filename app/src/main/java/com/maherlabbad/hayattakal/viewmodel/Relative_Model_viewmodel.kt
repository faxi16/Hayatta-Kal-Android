package com.maherlabbad.hayattakal.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.Relative_model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RelativeModelviewmodel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(getApplication(), Model_Database::class.java,"Models").build()

    private val ModelDao = db.userDao();

    val itemList = mutableStateOf<List<Relative_model>>(listOf())

    val selectedItem = mutableStateOf(Relative_model("",""))

    fun getItemList(){
        viewModelScope.launch(Dispatchers.IO) {
            itemList.value = ModelDao.getItemWithNameAndId()
        }
    }

    fun saveItem(item: Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            val relative = ModelDao.Contains(item.phone_number)
            if(relative == null){
                ModelDao.insert(item)
                getItemList()
            }else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), "Bu kişi zaten kayıtlı", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun deleteItem(item : Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            ModelDao.Delete(item)
            getItemList()
        }
    }
}