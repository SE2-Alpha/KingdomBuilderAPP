package at.aau.serg.websocketbrokerdemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerScoreDTO
import at.aau.serg.websocketbrokerdemo.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GameEndingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val scoresJson = intent.getStringExtra("scores_json")
        val scores: List<PlayerScoreDTO> = Gson().fromJson(
            scoresJson,
            object : TypeToken<List<PlayerScoreDTO>>() {}.type
        )

        setContent {
            GameEndingScreen(scores)
        }
    }
}

@Composable
fun GameEndingScreen(scores: List<PlayerScoreDTO>){
    val sortedScores = scores.sortedByDescending { it.points}
    val winnerId = sortedScores.firstOrNull()?.playerId

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        BackgroundWithTitle()
        Column {
            winnerId?.let {
                Text(
                    text = "🎉 Congratulations $it, you won!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            Scoreboard(sortedScores)
        }
    }
}

@Composable
fun Scoreboard(scores: List<PlayerScoreDTO>) {
    Column {
        scores.forEach { playerScore ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Player ID: ${playerScore.playerId}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${playerScore.points} Punkte",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
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