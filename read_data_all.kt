package com.example.jsonapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsonapp.ui.theme.JsonAppTheme
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JsonAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    
                    greet()
                    //Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun greet(modifier: Modifier = Modifier) {
    val jsonContent = readNameFromJson(R.raw.test)
    val addressObject = readNameAddressFromJson(R.raw.test)

   
    

    Box(modifier = Modifier
        .padding(50.dp)
        .background(color = Color.Blue)) {
        LazyColumn(modifier = Modifier.padding(20.dp)) {
            item {
               

                if (addressObject != null) {
                    Text(
                        //text = jsonContent.optString("address", "Unknown"),
                        text = addressObject.optString("add","Unknown") ?: "Failed to read jsin",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        color = Color.White
                    )
                }
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


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JsonAppTheme {
        Greeting("Android")
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
fun readNameAddressFromJson(resourceId: Int): JSONObject? {
    val context = LocalContext.current
    val jsonString = readJsonFile(context, resourceId) ?: ""
    val jsonObject = JSONObject(jsonString)
    return jsonObject.optJSONObject("address")
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
        null }}


fun writeJsonToStorage(context: Context, fileName: String, jsonData: String) {
    try {
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(jsonData.toByteArray())
        fileOutputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle file writing error
    }
}
