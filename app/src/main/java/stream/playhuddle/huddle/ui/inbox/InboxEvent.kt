package stream.playhuddle.huddle.ui.inbox

import android.net.Uri

sealed class InboxEvent {
    data class OnSendMessage(val message: String, val userId: String): InboxEvent()
    data class OnImageSend(val uris: List<Uri>, val userId: String): InboxEvent()
}
