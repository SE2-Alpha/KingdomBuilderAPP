package at.aau.serg.websocketbrokerdemo

import MyStomp
import android.content.Context
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
import androidx.lifecycle.Lifecycle
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.lobby.Room
import at.aau.serg.websocketbrokerdemo.core.model.lobby.RoomStatus
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerDAO
import com.example.myapplication.R
import kotlinx.coroutines.delay
import org.json.JSONArray

class LobbyActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyStomp.connect(context = this) {
            MyStomp.subscribeToTopic("/topic/lobby") { res ->
                System.out.println("lobby: "+res);
                parseRoomList(res)
            }
        }
        enableEdgeToEdge()
        setContent {
            LobbyScreen()
        }
        //mystomp.requestRooms()
    }

    //private lateinit var mystomp: MyStomp
    private var roomList by mutableStateOf<List<Room>>(emptyList())
    private var joinedRoomId by mutableStateOf<String?>(null)

    fun onRoomClick(roomId: String) {
        MyStomp.joinRoom(roomId)
        //joinedRoomId = roomId
        // Beispiel: Fülle Test-Spieler
        MyStomp.subscribeToStartMsg(joinedRoomId?: "0") { msg ->
            msg.players.forEach { player ->
                if (player.id == MyStomp.playerId)
                {
                    Player.localPlayer = Player(player.id, player.name, player.color, GameBoard())
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
        //val rooms = listOf(
        //    Room(id = 1, name = "Room A", size = 4, currentUsers = 2),
        //    Room(id = 2, name = "Room B", size = 4, currentUsers = 4),
        //    Room(id = 3, name = "Room C", size = 4, currentUsers = 1),
        //)
        //val rooms = roomList
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
                    LobbyView(roomList, context, Modifier.align(Alignment.Center))
                } else {
                    // Neue Ansicht: Spieler im Raum + Start/Verlassen Buttons
                    RoomView()
                }
            }
        }
    }

    @Composable
    fun LobbyView(rooms: List<Room>, context: Context, modifier: Modifier) {
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
                                    }) {
                                        Text(
                                            text = if (room.status == RoomStatus.STARTED) "gestartet" else if (room.status == RoomStatus.FINISHED) "beendet" else if (room.status == RoomStatus.WAITING && room.currentUsers < room.size) "Beitreten" else "Voll",
                                            color = if (room.status == RoomStatus.WAITING && room.currentUsers < room.size) Color.Green else Color.Red,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                                //Divider()
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
            Button(
                onClick = {
                    val intent = Intent(context, GameActivity::class.java)
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
                    Button(onClick = { leaveRoom() }) {
                        Text("Verlassen")
                    }
                    Button(onClick = {
                        startGame()
                        //startGameActivity()
                    }) {
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
                Player.localPlayer = Player(player.id, player.name, player.color, GameBoard())
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
                        joinedRoomId = obj.getString("id")
                        if(RoomStatus.valueOf(obj.getString("status")) == RoomStatus.STARTED) {
                            Log.e("LobbyActivity", "Raum ist bereits gestartet, starte GameActivity")
                            startGameActivity()
                        } else {
                            onRoomClick(joinedRoomId.toString())
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
        //intent.putStringArrayListExtra("PLAYER_LIST", ArrayList(playersInRoom))
        startActivity(intent)
    }

    fun getRoomById(id: String): Room? {
        return roomList.find { it.id == id }
    }

    fun isActivityReallyActive(): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

}