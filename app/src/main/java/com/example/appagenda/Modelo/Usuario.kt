package com.example.appagenda.Modelo

import com.example.appagenda.Modelo.Tarea.Tarea

data class Usuario (var nombre: String, var userName: String,  var password: String, var correo:String, val listaTareas: MutableList<Tarea> ) {

}