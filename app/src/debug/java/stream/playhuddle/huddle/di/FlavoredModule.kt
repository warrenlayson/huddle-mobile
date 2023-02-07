package stream.playhuddle.huddle.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlavoredModule {

    private val EMULATOR_HOST = "10.0.2.2"
    private val AUTH_EMULATOR_PORT = 9099
    private val FIRESTORE_EMULATOR_PORT = 8080
    private val STORAGE_EMULATOR_PORT = 9099

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth {
        return Firebase.auth.apply {
            useEmulator(EMULATOR_HOST, AUTH_EMULATOR_PORT)
        }

    }

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore.apply {
        useEmulator(EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
    }

    @Provides
    @Singleton
    fun providesStorage(): FirebaseStorage = Firebase.storage.apply {
        useEmulator(EMULATOR_HOST, STORAGE_EMULATOR_PORT)
    }


}