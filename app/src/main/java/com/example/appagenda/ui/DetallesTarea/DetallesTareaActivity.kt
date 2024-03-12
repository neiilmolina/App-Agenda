import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.databinding.ActivityDetallesTareaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetallesTareaActivity : AppCompatActivity() {

    private lateinit var listaTareasViewModel: ListaTareasViewModel
    private lateinit var binding: ActivityDetallesTareaBinding
    private var tareaId: String = ""

    companion object {
        const val POSICION_TAREA = "tarea_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaTareasViewModel = ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        val posicion: Int = intent.getIntExtra(POSICION_TAREA, -1)
        if (posicion != -1 ) {
            val listaTarea = listaTareasViewModel.obtenerListaTareas()
            val tarea = listaTarea[posicion]
            tareaId = tarea.id
            crearUI(tarea)
        }


        binding.btnEditar.setOnClickListener {
            val nuevaTarea = getTarea(tareaId)
            nuevaTarea.fecha?.let { it1 ->
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        TareasRespositorio.actualizarTarea(
                            nuevaTarea.id,
                            nuevaTarea.titulo,
                            nuevaTarea.descripcion,
                            it1
                        )
                        crearUI(nuevaTarea)
                    } catch (e: Exception) {
                        // Handle error
                    }
                }
            }
        }

        binding.btnEliminar.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    TareasRespositorio.eliminarTarea(tareaId)
                    finish()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    private fun crearUI(tarea: Tarea) {
        binding.etTitulo.setText(tarea.titulo)
        binding.etFecha.setText(tarea.fechaString)
        binding.etDescripcion.setText(tarea.descripcion)
    }

    private fun getTarea(id: String): Tarea {
        val titulo = binding.etTitulo.text.toString()
        val fechaString = binding.etFecha.text.toString()
        val fecha = Tarea.parsearFecha(this, fechaString)
        val descripcion = binding.etDescripcion.text.toString()

        return Tarea(id, titulo, fecha, fechaString, descripcion)
    }
}
