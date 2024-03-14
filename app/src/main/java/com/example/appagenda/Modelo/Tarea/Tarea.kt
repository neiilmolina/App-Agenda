package com.example.appagenda.Modelo.Tarea

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date

data class Tarea(
    var id: String,
    var titulo: String,
    var fecha: Date,
    var fechaString: String?,
    var descripcion: String
) {
    companion object {
        private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")

        fun convertirFechaString(fecha: Date): String? {
            return fecha?.let { formatoFecha.format(it) }
        }

        fun parsearFecha(context: Context, fechaString: String?): Date? {
            return try {
                val fecha = fechaString?.let { formatoFecha.parse(it) }
                if (fecha == null) {
                    Toast.makeText(context, "No hay fecha", Toast.LENGTH_SHORT).show()
                } else if(fecha < Date()){
                    Toast.makeText(context, "La fecha tiene que se a partir de hoy", Toast.LENGTH_SHORT).show()
                }
                fecha
            } catch (e: Exception) {
                Toast.makeText(context, "Formato de fecha inválido (dd/MM/yyyy)", Toast.LENGTH_SHORT).show()
                null
            }
        }
    }
}
