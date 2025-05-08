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


const val URI_Server = "ws://se2-demo.aau.at:53213/ws-kingdombuilder-broker"

const val WEBSOCKET_URI = URI_Server

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
}