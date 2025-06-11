package at.aau.serg.websocketbrokerdemo

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerScoreDTO
import at.aau.serg.websocketbrokerdemo.ui.theme.MyApplicationTheme
import com.example.myapplication.R
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
            //GameEndingScreen(scores)

            // Beispielhafte Testdaten
            val testScores = listOf(
                PlayerScoreDTO(playerId = "Alice", points = 85),
                PlayerScoreDTO(playerId = "Bob", points = 65),
                PlayerScoreDTO(playerId = "Charlie", points = 95),
                PlayerScoreDTO(playerId = "Dana", points = 40)
            )
            GameEndingScreen(testScores)
        }
    }
}

@Composable
fun GameEndingScreen(scores: List<PlayerScoreDTO>){
    val context = LocalContext.current
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
                    text = "🎉 Congratulations $it, you won! 🎉",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(bottom = 24.dp, top = 5.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Scoreboard(sortedScores)
        }
        Button(onClick = {
            val intent = Intent(context, StartMenuActivity::class.java)
            context.startActivity(intent)
        },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.beige_lobby_background),
                contentColor = colorResource(id = R.color.light_blue_900)
            ),
            shape = RoundedCornerShape(20.dp)
        ){
            Text(
                text = "Back to Start Screen",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Scoreboard(scores: List<PlayerScoreDTO>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(29.dp)
            .padding(top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        scores.forEach { playerScore ->

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.beige_lobby_background)
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Player ID: ${playerScore.playerId}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${playerScore.points} Punkte",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.End
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
fun GameEndingScreenPreviewWithTestData(){
    val testScores = listOf(
        PlayerScoreDTO(playerId = "Alice", points = 85),
        PlayerScoreDTO(playerId = "Bob", points = 65),
        PlayerScoreDTO(playerId = "Charlie", points = 95),
        PlayerScoreDTO(playerId = "Dana", points = 40)
    )
    GameEndingScreen(testScores)
}