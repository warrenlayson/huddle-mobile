package stream.playhuddle.huddle

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val NAVIGATE_TO = "navigate_to"
    }

    var loading: Boolean = true

    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { loading }
        }

        lifecycleScope.launch {
            mainViewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    loading = uiState is MainUiState.Loading

                    if (uiState is MainUiState.Success) {
                        createChannel(
                            getString(R.string.huddle_notification_channel_id),
                            getString(R.string.huddle_notification_name)
                        )


                        setContent {
                            val startingGraph = uiState.startingGraph
                            HuddleApp(startingGraph = startingGraph)
                        }
                    }
                }
        }


    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.huddle_notification_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}
