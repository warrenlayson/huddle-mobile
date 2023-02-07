package stream.playhuddle.huddle.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUserId: String get() = auth.currentUser?.uid.orEmpty()

    val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.let { User(id = it.uid) } ?: User())
            }

            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    fun getProfile(): Flow<Profile?> {
        return currentCollection().document(currentUserId).snapshots().map {
            Timber.d(it.toString())
            it.toObject(Profile::class.java)
        }
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String, profile: Profile) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.uid?.let { uid ->
            currentCollection().document(uid).set(profile)
        }

    }

    suspend fun deleteAccount() {
        auth.currentUser?.delete()?.await()
    }

    fun signOut() {
        auth.signOut()
    }

    private fun currentCollection(): CollectionReference = firestore.collection(USERS_COLLECTION)

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}