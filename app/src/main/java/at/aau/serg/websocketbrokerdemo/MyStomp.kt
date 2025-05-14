import android.os.Handler
import android.os.Looper
import android.util.Log
import at.aau.serg.websocketbrokerdemo.Callbacks
import at.aau.serg.websocketbrokerdemo.core.model.lobby.PlayerListDao
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import org.json.JSONObject
import java.util.UUID

const val URI_Physical = "ws://10.0.0.180:8080/ws-kingdombuilder-broker"
const val URI_Server = "ws://se2-demo.aau.at:53213/ws-kingdombuilder-broker"

const val WEBSOCKET_URI = URI_Physical //URI_Server

object MyStomp {
    private lateinit var client: StompClient
    private lateinit var session: StompSession
    private val scope = CoroutineScope(Dispatchers.IO)
    val playerId: String = UUID.randomUUID().toString()

    private val topicCallbacks = mutableMapOf<String, MutableList<(String) -> Unit>>()

    fun subscribeToTopic(topic: String, callback: (String) -> Unit) {
        Log.d("MyStomp", "Versuche, Topic zu abonnieren: $topic")
        if (!topicCallbacks.containsKey(topic)) {
            topicCallbacks[topic] = mutableListOf()
            scope.launch {
                try {
                    val flow = session.subscribeText(topic)
                    launch {
                        flow.collect { msg ->
                            Log.d("MyStomp", "Nachricht empfangen am Topic $topic: $msg") // <- HIER
                            Handler(Looper.getMainLooper()).post {
                                topicCallbacks[topic]?.forEach { it(msg) }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MyStomp", "Fehler bei Subscription am Topic $topic: ${e.message}")
                }
            }
        }
        topicCallbacks[topic]?.add(callback)
    }


    fun connect(forceReconnect: Boolean = false, onConnected: (() -> Unit)? = null) {
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
            session.sendText("/app/lobby/create", "{\"playerId\":\"$playerId\"}")
        }
    }

    fun joinRoom(roomId: String) {
        scope.launch {
            session.sendText("/app/lobby/join", "{\"playerId\":\"$playerId\", \"roomId\":\"$roomId\"}")
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
            "playerId": "$playerId"
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

    fun placeHouses(gameId: String) {
        val payload = """
        {
            "gameId": "$gameId",
            "playerId": "$playerId"
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

    fun subscribeToGameUpdates(roomId: String, callback: (String) -> Unit) {
        subscribeToTopic("/topic/game/$roomId", callback)
    }

    fun subscribeToStartMsg(roomId:String,callback: (PlayerListDao) -> Unit){
        subscribeToTopic("/topic/room/Init/$roomId"){
            val parsed = Gson().fromJson(it, PlayerListDao::class.java)
            callback(parsed)
        }
    }

}