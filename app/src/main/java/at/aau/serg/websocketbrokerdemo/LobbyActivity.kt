package at.aau.serg.websocketbrokerdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

class LobbyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }

    @Composable
    fun LobbyScreen() {
        Box (
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Hintergrundbild
            Image(
                painter = painterResource(id = R.drawable.start_menu_background),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = painterResource(id = R.drawable.title),
                contentDescription = "Titel",
                modifier = Modifier
                    .offset(x = 150.dp, y = (-30).dp)
                    .size(230.dp)
                    .align(Alignment.TopStart)
            )

            /*Image(
                painter = painterResource(id = R.drawable.lobby_background),
                contentDescription = "Background Image",
                //modifier = Modifier.fillMaxSize().padding(16.dp),
                contentScale = ContentScale.Crop
            )

             */

        }
    }
}
