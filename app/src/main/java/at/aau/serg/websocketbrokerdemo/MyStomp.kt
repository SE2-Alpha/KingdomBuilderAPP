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

private  val WEBSOCKET_URI = "ws://10.0.2.2:8080/websocket-example-broker";
class MyStomp(val callbacks: Callbacks) {

    private lateinit var topicFlow: Flow<String>
    private lateinit var collector:Job

    private lateinit var jsonFlow: Flow<String>
    private lateinit var jsonCollector:Job

    private lateinit var client:StompClient
    private lateinit var session: StompSession

    private lateinit var gameFlow: Flow<String>
    private lateinit var gameCollector: Job


    private val scope:CoroutineScope=CoroutineScope(Dispatchers.IO)
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

                // Verbindung zum Game-Updates Topic
                gameFlow = session.subscribeText("/topic/game-updates")
                gameCollector = scope.launch {
                    gameFlow.collect { msg ->
                        callback(msg)
                    }
                }


                callback("connected")
            }

    }
    private fun callback(msg:String){
        Handler(Looper.getMainLooper()).post{
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

    fun createGame(playerIds: List<String>) {
        val json = JSONObject()
        json.put("playerIds", playerIds)
        val jsonString = json.toString()

        scope.launch {
            session.sendText("/app/game/create", jsonString)
        }
    }

    fun placeHouse(gameId: String, playerId: String) {
        val json = JSONObject()
        json.put("gameId", gameId)
        json.put("playerId", playerId)
        json.put("actionType", "PLACE_HOUSE") // oder was du brauchst
        val jsonString = json.toString()

        scope.launch {
            session.sendText("/app/game/place-house", jsonString)
        }
    }

    fun endTurn(gameId: String, playerId: String) {
        val json = JSONObject()
        json.put("gameId", gameId)
        json.put("playerId", playerId)
        val jsonString = json.toString()

        scope.launch {
            session.sendText("/app/game/end-turn", jsonString)
        }
    }

    fun drawCard(gameId: String, playerId: String) {
        val json = JSONObject()
        json.put("gameId", gameId)
        json.put("playerId", playerId)
        val jsonString = json.toString()

        scope.launch {
            session.sendText("/app/game/draw-card", jsonString)
        }
    }



}