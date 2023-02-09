package stream.playhuddle.huddle.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val username: String = "",
    val message: String? = null,
    val imageUrl: List<String>? = null,
    @ServerTimestamp val timestamp: Date? = null
)