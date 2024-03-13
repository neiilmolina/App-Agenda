import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appagenda.Modelo.Tarea.Tarea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaTareasViewModel : ViewModel() {

    private var _listaTareas: List<Tarea> = emptyList()

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

    // Método para obtener la lista de tareas, ya sea directamente o mediante LiveData si lo prefieres
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
}
