package com.example.exam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exam.ui.theme.ExamTheme
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.lifecycle.lifecycleScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase


import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat

@Entity(tableName = "users")
data class User (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usersText = StringBuilder()
        // Use lifecycleScope to launch a coroutine
        lifecycleScope.launch(Dispatchers.IO) {
            // Ensure applicationContext is not null
            val context: Context = applicationContext ?: return@launch
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "database-name"
            ).build()

            val userDao = db.userDao()

            val user1 = User(id = 678, firstName = "Akhil", lastName = "Dominic")


            // Perform the database insertion
            userDao.insertAll(user1)

            // Optionally, retrieve users from the database
            val users: List<User> = userDao.getAll()

            for (user in users) {
                usersText.append("User ID: ${user.id}, Name: ${user.firstName} ${user.lastName}\n")
            }
        }

        setContent {
            ExamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Exam()
                    //Greeting(name = usersText.toString())
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

fun showNotification(context: Context, title: String, message: String) {
    // NotificationManager
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Notification channel (for Android Oreo and above)
    val channelId = "your_channel_id"
    val channelName = "Your Channel Name"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    // Notification builder
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setAutoCancel(true) // Dismisses the notification when tapped

    // Show the notification
    val notificationId = 1 // Change this ID if you have multiple notifications
    notificationManager.notify(notificationId, notificationBuilder.build())
}

@Composable
fun Exam(modifier: Modifier=Modifier)
{
    val context = LocalContext.current
    Box(modifier= Modifier
        .background(Color.Yellow)
        .padding(60.dp)) {
        Column(modifier= Modifier
            .fillMaxSize()
            .background(Color.Green)
            .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text("Hello",fontSize = 60.sp)
            Text("How are yuo")
            Image(painter = painterResource(R.drawable.dice_1), contentDescription = "alte" )
            Spacer(modifier = Modifier.height(120.dp))

            Button(onClick = { showNotification(context,"helo","Happy birthday Desh bros") }) {
                Text(text = "Roll")
            }
        }
    }
}





