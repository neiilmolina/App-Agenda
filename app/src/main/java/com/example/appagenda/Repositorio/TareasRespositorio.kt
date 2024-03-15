import android.util.Log
import com.example.appagenda.MainActivity
import com.example.appagenda.Modelo.Tarea.Tarea
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.math.log

object TareasRespositorio {
    private const val COLLECTION_PATH = "tarea"
    private const val TAG = "TareasRespositorio"
    private var auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    lateinit var user: FirebaseUser
    suspend fun agregarTarea(
        titulo: String,
        descripcion: String,
        fecha: Date,
        listaTareasViewModel: ListaTareasViewModel?
    ) {
        user = auth.currentUser!!
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
        user = auth.currentUser!!
        val listaTareas = mutableListOf<Tarea>()
        try {
            val result = db.collection(COLLECTION_PATH).get().await()
            for (document in result) {
                if (document.get("idUsuario")== user?.uid){
                    Log.i("dennis2 idDocument", document.get("idUsuario").toString())
                    Log.i("dennis2 idUser", user!!.uid)
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
        user = auth.currentUser!!
        val tarea = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha,
            "idUsuario" to auth.currentUser!!.uid
        )

        try {
            if (user != null) {
                db.collection(COLLECTION_PATH).document(idTarea).set(tarea).await()
                Log.d(TAG, "Tarea actualizada correctamente")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error al actualizar tarea", e)
            throw e // Propagate the exception
        }
    }

    suspend fun eliminarTarea(idTarea: String) {
        user = auth.currentUser!!
        try {
            db.collection(COLLECTION_PATH).document(idTarea).delete().await()
            Log.d(TAG, "Tarea eliminada correctamente")
        } catch (e: Exception) {
            Log.w(TAG, "Error al eliminar tarea", e)
            throw e // Propagate the exception
        }
    }
}
