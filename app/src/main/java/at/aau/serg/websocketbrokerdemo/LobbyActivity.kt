package at.aau.serg.websocketbrokerdemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

class LobbyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LobbyScreen()
        }
    }

    @Composable
    fun LobbyScreen() {

        val context = LocalContext.current

        Box (
            modifier = Modifier
                .fillMaxSize()
        ) {

            BackgroundWithTitle()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lobby_background),
                    contentDescription = "Background Image light",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                //Back Button (top left)
                Button(
                    onClick = {
                        val intent = Intent(context, StartMenuActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Arrow Back",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(450.dp)
                ) {
                    Box(){
                        Image(
                            painter = painterResource(id = R.drawable.lobby_background_upper),
                            contentDescription = "Background Image light",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 20.dp)
                        ) {
                            Column {
                                Text(text = stringResource(id = R.string.join_game),
                                    modifier = Modifier.align(Alignment.CenterHorizontally))
                            }

                            Spacer(modifier = Modifier.width(100.dp))

                            Column {
                                Text(
                                    text = stringResource(id = R.string.create_game),
                                    modifier = Modifier.align(Alignment.CenterHorizontally))

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {  },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(100.dp)
                                        .width(100.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                            contentPadding = PaddingValues(0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(20.dp))
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.plus_square_add_game),
                                            contentDescription = "Add Game",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            //GameActivity öffnen --> ändern
                            val intent = Intent(context, StartMenuActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(60.dp)
                            .width(170.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(20.dp))
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.button_grey),  //wenn Spiel gestartet werden kann, Farbe ändern
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.start_game),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
