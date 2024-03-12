import androidx.lifecycle.ViewModel
import com.example.appagenda.Modelo.Tarea.Tarea

class ListaTareasViewModel : ViewModel() {
    // Atributo privado para almacenar la lista de tareas
    private var _listaTareas: List<Tarea> = emptyList()

    // Getter público para acceder a la lista de tareas
    val listaTareas = _listaTareas

    init {
        // Llamar a la función obtenerTareas desde el constructor
        obtenerTareas()
    }

    /**
     * Obtiene la lista de tareas desde Firestore y la almacena en el atributo privado _listaTareas.
     */
    private fun obtenerTareas() {
        TareasRespositorio.obtenerTareas { listaTareas ->
            _listaTareas = listaTareas
            // Aquí puedes realizar cualquier acción adicional después de obtener las tareas
        }
    }
}
