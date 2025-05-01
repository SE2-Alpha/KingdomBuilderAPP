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

class MyStomp(val callbacks: Callbacks) {
const val WEBSOCKET_URI = "ws://10.0.2.2:8080/ws-kingdombuilder-broker";
// URL für den Uni-Server: ws://se2-demo.aau.at:53213/ws-kingdombuilder-broker

    private lateinit var topicFlow: Flow<String>
    private lateinit var collector:Job

    private lateinit var jsonFlow: Flow<String>
    private lateinit var jsonCollector:Job

    private lateinit var roomsFlow: Flow<String>
    private lateinit var roomsCollector:Job

    private lateinit var client:StompClient
    private lateinit var session: StompSession

    private val scope:CoroutineScope=CoroutineScope(Dispatchers.IO)

    // UUID als eindeutige ID
    val playerId: String = UUID.randomUUID().toString()


    fun connect() {

            client = StompClient(OkHttpWebSocketClient()) // other config can be passed in here
            scope.launch {
                session=client.connect(WEBSOCKET_URI)
                topicFlow= session.subscribeText("/topic/hello-response")
                //connect to topic
                collector=scope.launch { topicFlow.collect{
                        msg->
                    //todo logic
                    callback(msg)
                } }

                //connect to topic
                jsonFlow= session.subscribeText("/topic/rcv-object")
                jsonCollector=scope.launch { jsonFlow.collect{
                        msg->
                    var o=JSONObject(msg)
                    callback(o.get("text").toString())
                } }
                //callback("connected")

                roomsFlow = session.subscribeText("/topic/lobby")
                roomsCollector = scope.launch {
                    roomsFlow.collect { msg ->
                        callback(msg)  // JSON-Array als String an Activity schicken
                    }
                }

            }

    }
    private fun callback(msg:String){
        Handler(Looper.getMainLooper()).post{
            Log.e("tag","message from server: $msg")
            callbacks.onResponse(msg)
        }
    }
    fun sendHello(){

        scope.launch {
            Log.e("tag","connecting to topic")

            session.sendText("/app/hello","message from client")
           }
    }
    fun sendJson(){
        var json=JSONObject();
        json.put("from","client")
        json.put("text","from client")
        var o=json.toString()

        scope.launch {
            session.sendText("/app/object",o);
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

}