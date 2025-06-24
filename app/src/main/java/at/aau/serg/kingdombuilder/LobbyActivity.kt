package at.aau.serg.kingdombuilder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import at.aau.serg.kingdombuilder.core.model.lobby.Room
import at.aau.serg.kingdombuilder.core.model.lobby.RoomStatus
import at.aau.serg.kingdombuilder.core.model.player.Player
import at.aau.serg.kingdombuilder.core.model.player.PlayerDAO
import com.example.myapplication.R
import kotlinx.coroutines.delay
import org.json.JSONArray


class LobbyActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyStomp.connect(context = this) {
            MyStomp.subscribeToTopic("/topic/lobby") { res ->
                println("lobby: $res")
                parseRoomList(res)
            }
        }
        enableEdgeToEdge()
        setContent {
            LobbyScreen()
        }
    }

    //private lateinit var mystomp: MyStomp
    private var roomList by mutableStateOf<List<Room>>(emptyList())
    private var joinedRoomId by mutableStateOf<String?>(null)

    fun onRoomClick(roomId: String) {
        MyStomp.joinRoom(roomId)
        // Beispiel: Fülle Test-Spieler
        MyStomp.subscribeToStartMsg(joinedRoomId?: "0") { msg ->
            msg.players.forEach { player ->
                if (player.id == MyStomp.playerId)
                {
                    Player.localPlayer = Player(player.id, player.name, player.color)
                }
            }
        }
    }

    @Composable
    fun LobbyScreen() {

        LaunchedEffect(Unit) {
            while (true) {
                MyStomp.requestRooms()
                delay(5000) // alle 5 Sekunden aktualisieren
            }
        }

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
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.lobby_background))
            ) {


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

                if (joinedRoomId == null) {
                    // Bisherige Lobby: Raumliste und "Create Room"-Button
                    LobbyView(roomList, Modifier.align(Alignment.Center))
                } else {
                    // Neue Ansicht: Spieler im Raum + Start/Verlassen Buttons
                    RoomView()
                }
            }
        }
    }

    @Composable
    fun LobbyView(rooms: List<Room>, modifier: Modifier) {
        Column(
            modifier = modifier.width(450.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)                        // Höhe anpassen
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.lobby_background_upper))         // hier Deine Farbe
            ){


                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                ) {
                    Column (
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(id = R.string.join_game),
                            modifier = Modifier.align(Alignment.CenterHorizontally)

                        )
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            // Für jeden Raum eine Zeile
                            rooms.sortedBy { it.name }.forEach { room ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onRoomClick(room.id) }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Name + Belegung
                                    Column(
                                        modifier = Modifier.weight(1f)
                                            .padding(start = 8.dp)
                                    ) {
                                        Text(text = room.name)
                                        Text(
                                            text = "${room.currentUsers} / ${room.size} Spieler",
                                            color = Color.DarkGray
                                        )
                                    }
                                    // Status-Text
                                    Button(onClick = {
                                        onRoomClick(room.id)
                                    },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorResource(id = R.color.beige_lobby_background),
                                            contentColor = colorResource(id = R.color.light_blue_900),
                                            disabledContainerColor = colorResource(id = R.color.button_disabled),
                                            disabledContentColor = colorResource(id = R.color.button_disabled_text)
                                        ),) {
                                        Text(
                                            text = when (room.status) {
                                                RoomStatus.WAITING -> if(room.currentUsers < room.size) "Beitreten" else "Voll"
                                                RoomStatus.STARTED  -> "Gestartet"
                                                RoomStatus.FINISHED -> "Beendet"
                                            },
                                            color = if (room.status == RoomStatus.WAITING && room.currentUsers < room.size) colorResource(R.color.Green_Dark) else colorResource(R.color.Red_Dark),
                                        )
                                    }
                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.width(100.dp)
                        .weight(0.1f))

                    Column(modifier = Modifier.weight(1f)){
                        Text(
                            text = stringResource(id = R.string.create_game),
                            modifier = Modifier.align(Alignment.CenterHorizontally))

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                MyStomp.createRoom()
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .height(100.dp)
                                .width(100.dp)
                                .align(Alignment.CenterHorizontally),

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
        }
    }

    @Composable
    fun RoomView() {
        Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.lobby_background_upper))         // hier Deine Farbe
                ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val room = roomList.find { it.id == joinedRoomId }
                Text("Spieler im Raum: "+ room?.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                room?.players?.forEach { player ->
                    Text(player.name, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { leaveRoom() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.beige_lobby_background),
                            contentColor = colorResource(id = R.color.light_blue_900),
                            disabledContainerColor = colorResource(id = R.color.button_disabled),
                            disabledContentColor = colorResource(id = R.color.button_disabled_text)
                        ),
                    ) {
                        Text("Verlassen")
                    }
                    Button(
                        onClick = { startGame() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.beige_lobby_background),
                            contentColor = colorResource(id = R.color.light_blue_900),
                            disabledContainerColor = colorResource(id = R.color.button_disabled),
                            disabledContentColor = colorResource(id = R.color.button_disabled_text)
                        ),) {
                        Text("Starten")
                    }
                }
            }
        }
    }

    private fun leaveRoom() {
        MyStomp.leaveRoom(joinedRoomId!!)
        joinedRoomId = null
    }

    private fun startGame() {
        //Create Local Player, color is set trough start message
        getRoomById(joinedRoomId.toString())?.players?.forEach { player ->
            if (player.id == MyStomp.playerId) {
                Player.localPlayer = Player(player.id, player.name, player.color)
            }
        }
        MyStomp.startRoom(joinedRoomId!!)
    }

    private fun parseRoomList(res: String) {
        try {
            Log.e("LobbyActivity", "Nachricht vom Server: $res")
            val roomsJson = JSONArray(res)
            val updatedRooms = mutableListOf<Room>()

            for (i in 0 until roomsJson.length()) {
                val obj = roomsJson.getJSONObject(i)

                val playersJson = obj.getJSONArray("players")
                val players = mutableListOf<PlayerDAO>() // PlayerData ist ein Hilfsmodell, siehe unten

                for (j in 0 until playersJson.length()) {
                    val playerObj = playersJson.getJSONObject(j)
                    players.add(
                        PlayerDAO(
                            id = playerObj.getString("id"),
                            name = (if (!playerObj.isNull("name")) playerObj.getString("name") else null).toString(),
                            color = playerObj.getInt("color"),
                            remainingSettlements = playerObj.getInt("remainingSettlements"),
                            score = playerObj.getInt("score")
                        )
                    )
                    if(MyStomp.playerId == playerObj.getString("id") && isActivityReallyActive())
                    {
                        players.forEach { player ->
                            if (player.id == MyStomp.playerId) {
                                Player.localPlayer = Player(player.id, player.name, player.color)
                            }
                        }

                        if(RoomStatus.valueOf(obj.getString("status")) == RoomStatus.FINISHED)
                        {
                            MyStomp.leaveRoom(roomId = joinedRoomId.toString())
                            joinedRoomId = null
                        } else {
                            if (joinedRoomId == null) {
                                joinedRoomId = obj.getString("id")
                                onRoomClick(joinedRoomId.toString())
                            }
                            if (RoomStatus.valueOf(obj.getString("status")) == RoomStatus.STARTED) {
                                Log.e("LobbyActivity", "Raum ist bereits gestartet, starte GameActivity")
                                startGameActivity()
                            }
                        }
                    }
                }

                updatedRooms.add(
                    Room(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        size = obj.getInt("size"),
                        currentUsers = obj.getInt("currentUsers"),
                        status = RoomStatus.valueOf(obj.getString("status")),
                        players = players // Neue Zeile für die Liste der Spieler
                    )
                )

            }

            roomList = updatedRooms
        } catch (e: Exception) {
            Log.e("LobbyActivity", "Fehler beim Parsen der Räume", e)
        }
    }

    private fun startGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("ROOM_ID", joinedRoomId)
        startActivity(intent)
    }

    fun getRoomById(id: String): Room? {
        return roomList.find { it.id == id }
    }

    fun isActivityReallyActive(): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

}