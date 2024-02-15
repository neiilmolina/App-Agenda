package com.example.appagenda.ui.listaTareas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListaTareasViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is lista tarea"
    }
    val text: LiveData<String> = _text
}