package at.aau.serg.websocketbrokerdemo

import android.R.attr.fontStyle
import android.R.attr.fontWeight
import android.R.attr.id
import android.inputmethodservice.Keyboard.Row
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.aau.serg.websocketbrokerdemo.ui.theme.MyApplicationTheme
import com.example.myapplication.R

class LobbyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lobby()
        }
    }
}

@Composable
fun Lobby() {

    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        // Hintergrundbild
        Image(
            painter = painterResource(id = R.drawable.lobbyactivity),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = R.drawable.titel),
            contentDescription = "Small Icon",
            modifier = Modifier
                .offset(x = 150.dp, y = (-30).dp)
                .size(230.dp)
                .align(Alignment.TopStart)
        )


        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(8.dp)
                    .height(80.dp)
                    .width(170.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.beige_lobby_background),
                    contentColor = colorResource(id = R.color.light_blue_900)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.lets_go),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {

            //für Settings und Regeln Composable functions oder eigene Acitvities??

            Column {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(40.dp)
                        .width(100.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.beige_lobby_background),
                        contentColor = colorResource(id = R.color.light_blue_900)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = stringResource(id = R.string.settings))
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .height(40.dp)
                        .width(100.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.beige_lobby_background),
                        contentColor = colorResource(id = R.color.light_blue_900)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = stringResource(id = R.string.rules))
                }
            }
        }
    }

    /*Image(
        painter = painterResource(id = R.drawable.lobbyactivity),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
     */
}

@Preview(showBackground = true)
@Composable
fun LobbyPreview() {
    Lobby()
}