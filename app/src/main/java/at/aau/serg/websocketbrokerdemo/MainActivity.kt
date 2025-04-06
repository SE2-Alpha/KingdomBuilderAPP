package at.aau.serg.websocketbrokerdemo

import MyStomp
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlin.jvm.java


class MainActivity : ComponentActivity(), Callbacks {
    lateinit var mystomp:MyStomp

    var responseText by mutableStateOf("Waiting for response...")

    override fun onCreate(savedInstanceState: Bundle?) {
        mystomp=MyStomp(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConnectScreen()
        }
    }

    @Composable
    fun ConnectScreen() {

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.light_blue_600))
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    mystomp.connect()
                }) {
                    Text(text = "Connect")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    mystomp.sendHello()
                }) {
                    Text(text = "Send Hello")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    mystomp.sendJson()
                }) {
                    Text(text = "Send Json")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val intent = Intent(context, StartMenuActivity::class.java)
                    context.startActivity(intent)
                }){
                    Text(text = "Start Menu")
                }
            }
            Text(
                text = responseText,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }

    override fun onResponse(res: String) {
        responseText=res
    }

    @Preview(showBackground = true)
    @Composable
    fun ConnectScreenPreview() {
        ConnectScreen()
    }
}

