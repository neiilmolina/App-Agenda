import android.util.Log
import com.example.appagenda.Modelo.Tarea.Tarea
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

object TareasRespositorio {
    private const val COLLECTION_PATH = "tarea"

    private const val TAG = "TareasRespositorio"

    fun agregarTarea(titulo: String, descripcion: String, fecha: Date) {
        val db = FirebaseFirestore.getInstance()
        val tarea = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha
        )

        db.collection(COLLECTION_PATH)
            .add(tarea)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Tarea agregada con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al agregar tarea", e)
            }
    }

    /**
     * Obtiene todas las tareas de Firestore.
     * @param onComplete Callback que se llama cuando se completa la operación.
     */
    fun obtenerTareas(onComplete: (List<Tarea>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener { result ->
                val listaTareas = mutableListOf<Tarea>()
                for (document in result) {
                    val fechaTimestamp = document.getTimestamp("fecha")
                    val fecha = fechaTimestamp?.toDate() ?: Date()
                    val tarea = Tarea(
                        id = document.id,
                        titulo = document.getString("titulo") ?: "",
                        descripcion = document.getString("descripcion") ?: "",
                        fecha = fecha,
                        fechaString = Tarea.convertirFechaString(fecha)
                    )
                    listaTareas.add(tarea)
                }
                onComplete(listaTareas)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al obtener tarea", e)
                onComplete(emptyList()) // Devolver una lista vacía en caso de error
            }
    }


    fun obtenerTareaPorId(idTarea: String, onSuccess: (Tarea?) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH).document(idTarea)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val tarea = documentSnapshot.toObject(Tarea::class.java)
                onSuccess(tarea)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al obtener tarea por ID", e)
                onFailure(e)
            }
    }

    fun actualizarTarea(idTarea: String, titulo: String, descripcion: String, fecha: Date) {
        val db = FirebaseFirestore.getInstance()
        val tarea = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha
        )

        db.collection(COLLECTION_PATH).document(idTarea)
            .set(tarea)
            .addOnSuccessListener {
                Log.d(TAG, "Tarea actualizada correctamente")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al actualizar tarea", e)
            }
    }

    fun eliminarTarea(idTarea: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_PATH).document(idTarea)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Tarea eliminada correctamente")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al eliminar tarea", e)
            }
    }
}
