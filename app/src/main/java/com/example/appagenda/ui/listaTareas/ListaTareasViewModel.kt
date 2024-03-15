import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.Modelo.Tarea.Tarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class ListaTareasViewModel : ViewModel() {

    private var _listaTareas: List<Tarea> = mutableListOf()

    init {
        obtenerTareas()
    }

    fun obtenerTareas() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tareas = TareasRespositorio.obtenerTareas()
                _listaTareas = tareas
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, loggear el error
                e.printStackTrace()
            }
        }
    }

    fun obtenerListaTareas(): List<Tarea> {
        return _listaTareas
    }

    /**
     * Metodo el cual añade una nueva tarea en la lista de tareas
     * @param tarea modelo de tarea para añadir a la lista
     */
    fun addTareas(tarea: Tarea){
        _listaTareas = _listaTareas.plus(tarea)
    }

    fun editTarea(tarea: Tarea){
        _listaTareas = _listaTareas.map { t ->
            if (t.id == tarea.id) tarea else t
        }.toMutableList()
    }

    fun eliminarTarea(idTarea: String) {
        _listaTareas = _listaTareas.filter { t -> t.id != idTarea }.toMutableList()
    }

    fun vaciarLista(){
        _listaTareas = emptyList()
    }

    fun obtenerTareaMasProxima(tareas: List<Tarea>): Tarea? {
        val hoy = Calendar.getInstance()

        var tareaMasProxima: Tarea? = null
        var diferenciaMinima = Long.MAX_VALUE

        for (tarea in tareas) {
            val diferencia = tarea.fecha.time - hoy.timeInMillis
            if (diferencia >= 0 && diferencia < diferenciaMinima) {
                diferenciaMinima = diferencia
                tareaMasProxima = tarea
            }
        }
        return tareaMasProxima
    }

}
