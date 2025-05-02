import android.os.Handler
import android.os.Looper
import android.util.Log
import at.aau.serg.websocketbrokerdemo.Callbacks
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

const val WEBSOCKET_URI = "ws://192.168.137.1:8080/ws-kingdombuilder-broker";
//const val WEBSOCKET_URI = "ws://10.0.2.2:8080/ws-kingdombuilder-broker";
// URL für den Uni-Server: ws://se2-demo.aau.at:53213/ws-kingdombuilder-broker

object MyStomp {
    private lateinit var client: StompClient
    private lateinit var session: StompSession
    private val scope = CoroutineScope(Dispatchers.IO)
    val playerId: String = UUID.randomUUID().toString()

    private val topicCallbacks = mutableMapOf<String, MutableList<(String) -> Unit>>()

    fun subscribeToTopic(topic: String, callback: (String) -> Unit) {
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
        scope.launch {
            session.sendText("/app/hello", "message from client")
        }
    }

    fun send(Subject: String, message: String) {
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

    fun drawCard(gameId: String, playerId: String) {
        val message = JSONObject().apply {
            put("roomId", gameId)
            put("playerId", playerId)
        }
        send("/app/game/drawCard", message.toString())
    }

    fun placeHouses(gameId: String, playerId: String) {
        val message = JSONObject().apply {
            put("roomId", gameId)
            put("playerId", playerId)
        }
        send("/app/game/placeHouses", message.toString())
    }

    fun endTurn(gameId: String, playerId: String) {
        val message = JSONObject().apply {
            put("roomId", gameId)
            put("playerId", playerId)
        }
        send("/app/game/endTurn", message.toString())
    }

    fun subscribeToGameUpdates(roomId: String, callback: (String) -> Unit) {
        subscribeToTopic("/topic/game/$roomId", callback)
    }

}