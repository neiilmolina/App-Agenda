import android.util.Log
import com.example.appagenda.Modelo.Tarea.Tarea
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

object TareasRespositorio {
    private const val COLLECTION_PATH = "tarea"
    private const val TAG = "TareasRespositorio"

    private val db = FirebaseFirestore.getInstance()
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    suspend fun agregarTarea(
        titulo: String,
        descripcion: String,
        fecha: Date,
        listaTareasViewModel: ListaTareasViewModel?
    ) {
        val tarea = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha,
            "idUsuario" to user!!.uid
        )

        try {
            val documentReference = db.collection(COLLECTION_PATH).add(tarea).await()
            if (user != null) {
                listaTareasViewModel?.addTareas(Tarea(
                    id = documentReference.id,
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = fecha,
                    fechaString = Tarea.convertirFechaString(fecha),
                    idUsuario = user.uid
                ))
            }
            Log.d(TAG, "Tarea agregada con ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.w(TAG, "Error al agregar tarea", e)
            throw e // Propagate the exception
        }
    }

    suspend fun obtenerTareas(): List<Tarea> {
        val listaTareas = mutableListOf<Tarea>()
        try {
            val result = db.collection(COLLECTION_PATH).get().await()
            for (document in result) {
                if (document.get("idUsuario")== user?.uid){
                    val fechaTimestamp = document.getTimestamp("fecha")
                    val fecha = fechaTimestamp?.toDate() ?: Date()
                    val tarea = user?.let {
                        Tarea(
                            id = document.id,
                            titulo = document.getString("titulo") ?: "",
                            descripcion = document.getString("descripcion") ?: "",
                            fecha = fecha,
                            fechaString = Tarea.convertirFechaString(fecha),
                            idUsuario = it.uid
                        )
                    }
                    if (tarea != null) {
                        listaTareas.add(tarea)
                    }
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener tareas", e)
            throw e // Propagate the exception
        }
        return listaTareas
    }

    suspend fun actualizarTarea(idTarea: String, titulo: String, descripcion: String, fecha: Date) {
        val tarea = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha
        )

        try {
            if (user != null) {
                db.collection(COLLECTION_PATH).document(idTarea).update(tarea as Map<String, Any>)
                    .await()
                Log.d(TAG, "Tarea actualizada correctamente")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error al actualizar tarea", e)
            throw e // Propagate the exception
        }
    }

    suspend fun eliminarTarea(idTarea: String) {
        try {
            db.collection(COLLECTION_PATH).document(idTarea).delete().await()
            Log.d(TAG, "Tarea eliminada correctamente")
        } catch (e: Exception) {
            Log.w(TAG, "Error al eliminar tarea", e)
            throw e // Propagate the exception
        }
    }
}
