import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import at.aau.serg.websocketbrokerdemo.core.model.lobby.PlayerListDAO
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.util.UUID

const val URI_Physical = "ws://10.0.2.2:8080/ws-kingdombuilder-broker"
const val URI_Server = "ws://se2-demo.aau.at:53213/ws-kingdombuilder-broker"

const val WEBSOCKET_URI = URI_Physical //URI_Server

object MyStomp {
    private lateinit var client: StompClient
    private lateinit var session: StompSession
    private val scope = CoroutineScope(Dispatchers.IO)
    var playerId: String = ""
    var userName: String = ""
    var playerIsActive by mutableStateOf(false)

    fun setPlayerActive(isActive: Boolean) {
        playerIsActive = isActive
    }



    private val topicCallbacks = mutableMapOf<String, MutableList<(String) -> Unit>>()

    fun subscribeToTopic(topic: String, callback: (String) -> Unit) {
        Log.d("MyStomp", "Versuche, Topic zu abonnieren: $topic")
        if (!topicCallbacks.containsKey(topic)) {
            topicCallbacks[topic] = mutableListOf()
            // Erstes Mal: STOMP-Subscription starten
            scope.launch {
                val flow = session.subscribeText(topic)
                launch {
                    flow.collect { msg ->
                        Handler(Looper.getMainLooper()).post {
                            topicCallbacks[topic]?.forEach { it(msg) }
                        }
                    }
                }
            }
        }
        topicCallbacks[topic]?.add(callback)
    }

    fun connect(context: Context,forceReconnect: Boolean = false, onConnected: (() -> Unit)? = null) {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        userName = prefs.getString("user_name", "") ?: ""
        playerId = prefs.getString("player_id", UUID.randomUUID().toString()) ?: UUID.randomUUID().toString()
        val editor = prefs.edit()
        editor.putString("player_id", playerId)
        editor.apply()

        if (::session.isInitialized && !forceReconnect) {
            Handler(Looper.getMainLooper()).post {
                onConnected?.invoke()
            }
            return
        }

        client = StompClient(OkHttpWebSocketClient())
        scope.launch {
            session = client.connect(WEBSOCKET_URI)
            Handler(Looper.getMainLooper()).post {
                onConnected?.invoke()
            }
        }
    }


    fun sendHello() {
        Log.d("MyStomp", "Sende Hello Nachricht an Server")
        scope.launch {
            session.sendText("/app/hello", "message from client")
        }
    }

    fun send(Subject: String, message: String) {
        Log.d("MyStomp", "send an Server.")
        scope.launch {
            session.sendText(Subject, message)
        }
    }

    fun requestRooms() {
        scope.launch {
            session.sendText("/app/lobby/get", "")
        }
    }

    fun createRoom() {
        scope.launch {
            session.sendText("/app/lobby/create", "{\"playerId\":\"$playerId\", \"userName\":\"$userName\"}")
        }
    }

    fun joinRoom(roomId: String) {
        scope.launch {
            session.sendText("/app/lobby/join", "{\"playerId\":\"$playerId\", \"roomId\":\"$roomId\", \"userName\":\"$userName\"}")
        }
    }

    fun leaveRoom(roomId: String) {
        scope.launch {
            session.sendText("/app/lobby/leave", "{\"playerId\":\"$playerId\", \"roomId\":\"$roomId\"}")
        }
    }

    fun startRoom(roomId: String) {
        scope.launch {
            session.sendText("/app/lobby/start", "{\"playerId\":\"$playerId\", \"roomId\":\"$roomId\"}")
        }
    }

    fun drawCard(gameId: String) {
        val payload = """
        {
            "gameId": "$gameId",
            "playerId": "$playerId",
            "playerIsActive": "$playerIsActive"
        }
    """.trimIndent()

        Log.d("MyStomp", "Sende DrawCard Nachricht: $payload")

        scope.launch {
            try {
                session.sendText("/app/game/drawCard", payload)
                Log.d("MyStomp", "DrawCard Nachricht gesendet an /app/game/drawCard")
            } catch (e: Exception) {
                Log.e("MyStomp", "Fehler beim Senden von DrawCard: ${e.message}")
            }
        }
    }

    fun placeHouses(gameId: String,) {
        val payload = """
        {
            "gameId": "$gameId",
            "playerId": "$playerId",
            "type": "PLACE_HOUSE",
            "position": {
                "x": 0,
                "y": 0
            }
        }
    """.trimIndent()

        Log.d("MyStomp", "Sende PlaceHouses Nachricht: $payload")

        scope.launch {
            try {
                session.sendText("/app/game/placeHouses", payload)
                Log.d("MyStomp", "PlaceHouses Nachricht gesendet an /app/game/placeHouses")
            } catch (e: Exception) {
                Log.e("MyStomp", "Fehler beim Senden von PlaceHouses: ${e.message}")
            }
        }
    }
    fun placeHouses(gameId: String,row: Int, column: Int) {
        val payload = """
        {
            "gameId": "$gameId",
            "playerId": "$playerId",
            "type": "PLACE_HOUSE",
            "position": {
                "x": "$column",
                "y": "$row"
            }
        }
    """.trimIndent()

        Log.d("MyStomp", "Sende PlaceHouses Nachricht: $payload")

        scope.launch {
            try {
                session.sendText("/app/game/placeHouses", payload)
                Log.d("MyStomp", "PlaceHouses Nachricht gesendet an /app/game/placeHouses")
            } catch (e: Exception) {
                Log.e("MyStomp", "Fehler beim Senden von PlaceHouses: ${e.message}")
            }
        }
    }

    fun endTurn(gameId: String) {
        val payload = """
        {
            "gameId": "$gameId",
            "playerId": "$playerId"
        }
    """.trimIndent()

        Log.d("MyStomp", "Sende EndTurn Nachricht: $payload")

        scope.launch {
            try {
                session.sendText("/app/game/endTurn", payload)
                Log.d("MyStomp", "EndTurn Nachricht gesendet an /app/game/endTurn")
            } catch (e: Exception) {
                Log.e("MyStomp", "Fehler beim Senden von EndTurn: ${e.message}")
            }
        }
    }

    fun subscribeToGameUpdatesTerrainCard(roomId: String, callback: (String) -> Unit) {
        subscribeToTopic("/topic/game/card/$roomId", callback)
    }

    fun subscribeToGameUpdates(roomId: String, callback: (String) -> Unit) {
        subscribeToTopic("/topic/game/update/$roomId", callback)
    }

    fun getGameUpdate(roomId: String) {
        scope.launch {
            session.sendText("/app/game/get", roomId)
        }
    }

    fun subscribeToStartMsg(roomId:String,callback: (PlayerListDAO) -> Unit){
        subscribeToTopic("/topic/room/Init/$roomId"){
            val parsed = Gson().fromJson(it, PlayerListDAO::class.java)
            callback(parsed)
        }
    }

}