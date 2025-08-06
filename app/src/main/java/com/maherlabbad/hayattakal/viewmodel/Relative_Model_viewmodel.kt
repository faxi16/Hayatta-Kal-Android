package com.maherlabbad.hayattakal.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.Relative_model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Relative_Model_viewmodel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(context = getApplication(), klass = Model_Database::class.java, "Model_Database").build()

    private val ModelDao = db.userDao();

    val itemlist = mutableStateOf<List<Relative_model>>(listOf())

    val selectedItem = mutableStateOf(Relative_model("",""))

    fun getItemList(){
        viewModelScope.launch(Dispatchers.IO) {
            itemlist.value = ModelDao.getItemWithNameAndId()
        }
    }

    fun saveItem(item: Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            ModelDao.insert(item)
        }
    }

    fun deleteItem(item : Relative_model){
        viewModelScope.launch(Dispatchers.IO) {
            ModelDao.Delete(item)
        }
    }
}