package stream.playhuddle.huddle.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firestore.v1.DocumentTransform.FieldTransform.ServerValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) {

    val messages: Flow<List<Message>>
        get() =
            currentCollection()
                .orderBy("timestamp")
                .snapshots()
                .map { it.toObjects() }

    suspend fun sendMessage(message: Message) {
        currentCollection().add(message.copy(
        )).await()
    }

    suspend fun sendImageMessage(message: Message, uris: List<Uri>) {
        val documentRef = currentCollection().add(message).await()

        try {
            val id = documentRef.id
            val storageRef = storage
                .getReference(auth.currentUser!!.uid)
                .child(id)
            val downloadUrls =
                uris.map { uri ->
                    putImageStorage(storageRef, uri).toString()
                }
            currentCollection().document(id).update(
                hashMapOf(
                    "imageUrl" to downloadUrls
                ) as Map<String, Any>
            )
        } catch (e: StorageException) {
            Timber.e(e)
        }

    }

    private suspend fun putImageStorage(storageReference: StorageReference, uri: Uri): Uri {

        val sd = uri.getFileName(context)

        val result = storageReference
            .child("file/$sd")
            .putFile(uri).await()

        Timber.d(result.toString())

        val downloadUrl = result.storage.downloadUrl.await()

        Timber.d(downloadUrl.toString())

        return downloadUrl

    }

    private fun currentCollection(): CollectionReference = firestore.collection(MESSAGES_CHILD)

    companion object {
        private const val MESSAGES_CHILD = "messages"
    }
}

@SuppressLint("Range")
fun Uri.getFileName(context: Context): String? {
    if (scheme == "content") {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        cursor.use {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
    }

    return path?.lastIndexOf('/')?.let { path?.substring(it) }
}