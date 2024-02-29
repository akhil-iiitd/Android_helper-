package com.example.dhiraj_pandey_project

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dhiraj_pandey_project.ui.theme.Dhiraj_pandey_projectTheme
import org.json.JSONObject
import java.io.InputStream
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dhiraj_pandey_projectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Red
                ) {
                    //Greeting("Android")
                    //greet()
                    lateinit var notificationHelper: NotificationHelper

                    fun onCreate(savedInstanceState: Bundle?) {
                        onCreate(savedInstanceState)
                        setContentView(R.layout.activity_main)

                        // Initialize the notification helper
                        notificationHelper = NotificationHelper(this)

                        // Create and show the notification
                        notificationHelper.createNotificationChannel()
                        val notification = notificationHelper.createNotification()
                        notificationHelper.showNotification(notification)

                }
            }
        }
    }

}
@Composable
fun greet(modifier: Modifier = Modifier) {
    val jsonContent = readNameFromJson(R.raw.test)

    Box(modifier = Modifier.padding(50.dp).background(color = Color.Blue)) {
        LazyColumn(modifier = Modifier.padding(20.dp)) {
            item {
                Text(
                    text = jsonContent.optString("name", "Unknown") ?: "Failed to read JSON file",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    color = Color.White
                )

                Text(
                    text = jsonContent.optString("age", "Unknown") ?: "Failed to read JSON file",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    color = Color.White
                )
                repeat(30) { index ->

                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White
                    )

                }
            }
        }
    }
}

class NotificationHelper(private val context: Context) {

    // Notification channel ID
    private val CHANNEL_ID = "my_channel_id"

    // Notification ID
    private val NOTIFICATION_ID = 123

    // Create a notification channel
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "My notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Create the notification
    fun createNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("My Notification")
            .setContentText("This is a notification created with Kotlin.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    // Show the notification
    fun showNotification(notification: NotificationCompat.Builder) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // You can handle the lack of permission here
            // You may want to request permission or handle the lack of permission differently
            return
        }

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}

@Composable
fun readNameFromJson(resourceId: Int): JSONObject {
    val context = LocalContext.current
    val jsonString = readJsonFile(context, resourceId) ?: ""
    val jsonObject = JSONObject(jsonString)
    return jsonObject
}


@Composable
fun readJsonFile(context: Context, resourceId: Int): String? {
    return try {
        val inputStream: InputStream = context.resources.openRawResource(resourceId)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        String(buffer)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }}}

