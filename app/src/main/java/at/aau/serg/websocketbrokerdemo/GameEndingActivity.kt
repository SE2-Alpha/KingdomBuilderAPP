package at.aau.serg.websocketbrokerdemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerScoreDTO
import at.aau.serg.websocketbrokerdemo.ui.theme.MyApplicationTheme

class GameEndingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun GameEndingScreen(scores: List<PlayerScoreDTO>){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        BackgroundWithTitle()
    }
}

@Preview(
    name = "Landscape Preview",
    showBackground = true,
    widthDp = 640,
    heightDp = 360,
    uiMode = Configuration.ORIENTATION_LANDSCAPE
)
@Composable
fun GameEndingScreenPreview(){
    GameEndingScreen(listOf())
}