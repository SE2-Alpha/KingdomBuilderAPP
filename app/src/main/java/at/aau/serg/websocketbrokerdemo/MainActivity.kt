package at.aau.serg.websocketbrokerdemo

import MyStomp
import android.os.Bundle
import android.widget.TextView
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


class MainActivity : ComponentActivity(), Callbacks {
    lateinit var mystomp:MyStomp
    /* XML-Ansatz
    lateinit var  response:TextView
     */

    var responseText by mutableStateOf("Waiting for response...")

    override fun onCreate(savedInstanceState: Bundle?) {
        mystomp=MyStomp(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConnectScreen()
        }

        /* XML-Ansatz
        setContentView(R.layout.fragment_fullscreen)

        findViewById<Button>(R.id.connectbtn).setOnClickListener { mystomp.connect() }
        findViewById<Button>(R.id.hellobtn).setOnClickListener{mystomp.sendHello()}
        findViewById<Button>(R.id.jsonbtn).setOnClickListener{mystomp.sendJson()}
        response=findViewById(R.id.response_view)
         */
    }

    /* XML-Ansatz
    override fun onResponse(res: String) {
        response.setText(res)
    }

     */

    @Composable
    fun ConnectScreen() {

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
                    responseText = "connected"
                    onResponse(responseText.toString())
                }) {
                    Text(text = "Connect")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    mystomp.sendHello()
                    responseText = "hello"
                    onResponse(responseText.toString())
                }) {
                    Text(text = "Send Hello")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    mystomp.sendJson()
                    responseText = "json"
                    onResponse(responseText.toString())
                }) {
                    Text(text = "Send Json")
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

