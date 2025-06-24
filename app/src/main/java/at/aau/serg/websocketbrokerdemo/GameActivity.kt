package at.aau.serg.websocketbrokerdemo

import androidx.activity.viewModels
import MyStomp
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.game.GameViewModel
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerDAO
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size

// Datenklasse für ein Hexagon-Feld
data class Hexagon(
    val row: Int,         // Gesamtzeile im 20x20-Gitter
    val col: Int,         // Gesamtspalte im 20x20-Gitter
    val centerX: Float,   // Berechneter Mittelpunkt (x)
    val centerY: Float,   // Berechneter Mittelpunkt (y)
    val quadrant: String, // Zugehöriges Quartil ("Quadrant 1" bis "Quadrant 4")
    val field: TerrainField
)

// Bestimmt das Quartil anhand von Zeile und Spalte im 20x20-Gitter
fun getQuadrant(row: Int, col: Int): String = when {
    row < 10 && col < 10 -> "Quadrant 1" // oben links
    row < 10 && col >= 10 -> "Quadrant 2" // oben rechts
    row >= 10 && col < 10 -> "Quadrant 3" // unten links
    else -> "Quadrant 4"                 // unten rechts
}

// Erzeugt einen Pfad für ein pointy-top Hexagon anhand des Mittelpunkts und der Seitenlänge
fun createHexagonPath(centerX: Float, centerY: Float, side: Float): Path {
    return Path().apply {
        for (i in 0 until 6) {
            val angleDeg = 90 + i * 60
            val angleRad = Math.toRadians(angleDeg.toDouble())
            val x = centerX + side * cos(angleRad).toFloat()
            val y = centerY + side * sin(angleRad).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}

// Prüft, ob ein Punkt (x, y) im Hexagon liegt (ohne Transformation)
fun pointInHexagon(x: Float, y: Float, centerX: Float, centerY: Float, side: Float): Boolean {
    val dx = abs(x - centerX)
    val dy = abs(y - centerY)
    if (dx > side * sqrt(3f) / 2 || dy > side) return false
    return side * sqrt(3f) / 2 - dx >= dy / 2
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HexagonBoardScreen(
    roomId: String,
    onDrawCard: (String) -> Unit,
    onPlaceHouses: (String) -> Unit,
    onEndTurn: (String) -> Unit,
    terrainCardType: String?,
    gameBoard: GameBoard,
    players: List<PlayerDAO>,
    activePlayer: PlayerDAO? = null,

    isCheatModeActive: Boolean,
    onToggleCheatMode: () -> Unit,
    onHousePlaced: (isCheated: Boolean) -> Unit,
    isReportWindowActive: Boolean,
    onReportPlayer: () -> Unit
) {
    val context = LocalContext.current
    // Zustand: Wenn kein Quartil ausgewählt, ist der Übersichtsmodus aktiv,
    // ansonsten wird in das entsprechende Quartil "gezoomt"
    var selectedQuadrant by remember { mutableStateOf<String?>(null) }
    // Speichert den Markierungsstatus einzelner Felder (Schlüssel: Triple(quadrant, localRow, localCol))
    val markedFields = remember { mutableStateMapOf<Pair<Int, Int>, Boolean>() }
    //val gameBoard = remember { GameBoard() }
    var houseIcon = rememberVectorPainter(Icons.Rounded.Home)
    //gameBoard.buildGameboard()

    val playerIsActive by remember { derivedStateOf { MyStomp.playerIsActive } }
    var drawCardIsClicked by remember { mutableStateOf(false) }
    var gotfirstUpdate by remember { mutableStateOf(false) }

    // Parameter für Übersichtsmodus (20x20 Felder)
    val overviewRows = 20
    val overviewCols = 20
    val overviewSide = 30f

    // Parameter für Zoommodus (10x10 Felder pro Quartil)
    val quadrantRows = 10
    val quadrantCols = 10
    val quadrantSide = 40f  // Größere Felder im Zoommodus

    // Berechne anhand des Modus die Liste der Hexagone
    val hexagons = remember(selectedQuadrant) {
        if (selectedQuadrant == null) {
            // Übersichtsmodus: 20x20-Gitter
            List(overviewRows * overviewCols) { index ->
                val row = index / overviewCols
                val col = index % overviewCols
                val centerX = overviewSide * sqrt(3f) * (col + if (row % 2 == 1) 0.5f else 0f)
                val centerY = overviewSide * 1.5f * row + overviewSide
                Hexagon(
                    row,
                    col,
                    centerX,
                    centerY,
                    getQuadrant(row, col),
                    gameBoard.getFieldByRowAndCol(row, col)
                )
            }
        } else {
            // Zoommodus: 10x10-Gitter des ausgewählten Quartils
            val (rowOffset, colOffset) = when (selectedQuadrant) {
                "Quadrant 1" -> 0 to 0
                "Quadrant 2" -> 0 to 10
                "Quadrant 3" -> 10 to 0
                "Quadrant 4" -> 10 to 10
                else -> 0 to 0
            }
            List(quadrantRows * quadrantCols) { index ->
                val localRow = index / quadrantCols  // 0 bis 9
                val localCol = index % quadrantCols    // 0 bis 9
                // Originalkoordinaten im 20x20-Gitter
                val row = localRow + rowOffset
                val col = localCol + colOffset
                // Neuberechnung der Positionen, sodass das Quartil zentriert angezeigt wird
                val centerX =
                    quadrantSide * sqrt(3f) * (localCol + if (localRow % 2 == 1) 0.5f else 0f)
                val centerY = quadrantSide * 1.5f * localRow + quadrantSide
                Hexagon(
                    row,
                    col,
                    centerX,
                    centerY,
                    selectedQuadrant!!,
                    gameBoard.getFieldByRowAndCol(row, col)
                )
            }
        }
    }

    // Das Board zentrieren: Wir berechnen die Bounding Box der Hexagone und ermitteln den Versatz.
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = constraints.maxWidth.toFloat()
        val canvasHeight = constraints.maxHeight.toFloat()
        val side = if (selectedQuadrant == null) overviewSide else quadrantSide

        val minX = hexagons.minOf { it.centerX } - side
        val maxX = hexagons.maxOf { it.centerX } + side
        val minY = hexagons.minOf { it.centerY } - side
        val maxY = hexagons.maxOf { it.centerY } + side
        val boardWidth = maxX - minX
        val boardHeight = maxY - minY
        val offsetX = (canvasWidth - boardWidth) / 2 - minX
        val offsetY = (canvasHeight - boardHeight) / 2 - minY

        Box(modifier = Modifier.fillMaxSize()) {
            // Der "Zurück"-Button als echter Button mit zIndex, damit er über dem Canvas liegt.
            if (selectedQuadrant != null) {
                Button(
                    onClick = { selectedQuadrant = null },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            // Canvas zum Zeichnen und Erfassen von Klicks
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(selectedQuadrant, isCheatModeActive) {
                        detectTapGestures { tapOffset: Offset ->
                            // Transformation des Tap-Offsets, damit die Trefferprüfung mit dem zentrierten Board funktioniert.
                            val transformedOffset =
                                Offset(tapOffset.x - offsetX, tapOffset.y - offsetY)
                            // Im Zoommodus: Bestimme den rowOffset/colOffset
                            val (rowOffset, colOffset) = if (selectedQuadrant != null) {
                                when (selectedQuadrant) {
                                    "Quadrant 1" -> 0 to 0
                                    "Quadrant 2" -> 0 to 10
                                    "Quadrant 3" -> 10 to 0
                                    "Quadrant 4" -> 10 to 10
                                    else -> 0 to 0
                                }
                            } else 0 to 0
                            // Prüfe alle Hexagone, ob der getippte Punkt darin liegt
                            hexagons.forEach { hex ->
                                if (pointInHexagon(
                                        transformedOffset.x,
                                        transformedOffset.y,
                                        hex.centerX,
                                        hex.centerY,
                                        side
                                    )
                                ) {
                                    if (selectedQuadrant == null) {
                                        // Übersichtsmodus: Zoom in das angeklickte Quartil
                                        selectedQuadrant = hex.quadrant
                                        println("Zoom in ${hex.quadrant}")
                                    } else {
                                        // Zoommodus: Toggle Markierung des Felds (schwarz/weiß)
                                        val localRow = hex.row - rowOffset
                                        val localCol = hex.col - colOffset
                                        MyStomp.placeHouses(roomId,hex.row,hex.col)
                                        val key = Pair(localRow, localCol)
                                        val currentlyMarked = gameBoard.getFieldByRowAndCol(hex.row, hex.col).builtBy != null
                                        // Prüfen, ob normal oder via Cheat platziert werden darf
                                        val canPlaceNormally = hex.field.isBuildable
                                        val canPlaceWithCheat = isCheatModeActive

                                        Log.i("Player Interaction","Field ${hex.row}, ${hex.col} in ${hex.quadrant} toggled to ${!currentlyMarked}")
                                    }
                                    return@detectTapGestures
                                }
                            }
                        }
                    }
            ) {

                // Zeichnen des Spielfelds mit Translation zum Zentrieren
                translate(left = offsetX, top = offsetY) {
                    hexagons.forEach { hex ->
                        val side = if (selectedQuadrant == null) overviewSide else quadrantSide
                        val hexPath = createHexagonPath(hex.centerX, hex.centerY, side)
                        // Im Übersichtsmodus: Einheitliche Füllfarbe, im Zoommodus abhängig vom Markierungsstatus

                        val (rowOffset, colOffset) = when (hex.quadrant) {
                            "Quadrant 1" -> 0 to 0
                            "Quadrant 2" -> 0 to 10
                            "Quadrant 3" -> 10 to 0
                            "Quadrant 4" -> 10 to 10
                            else -> 0 to 0
                        }
                        val fillColor = gameBoard.getFieldByRowAndCol(hex.row,hex.col).getColor(context) // Hexagon-Farbe
                        // Zuerst Füllung, dann Kontur zeichnen
                        drawPath(path = hexPath, color = fillColor)
                        drawPath(path = hexPath, color = Color.Black, style = Stroke(width = 2f))
                        //Falls Feld besetzt, Gebäude Zeichnen
                        if(gameBoard.getFieldByRowAndCol(hex.row,hex.col).builtBy != null) {
                            System.out.println("Draw field with building at ${hex.row}, ${hex.col} in ${hex.quadrant}")
                            //if(drawCardIsClicked) {
                            drawIntoCanvas { canvas ->
                                val iconSize = 55f
                                val thisField = gameBoard.getFieldByRowAndCol(hex.row, hex.col)
                                Log.i(
                                    "GameActivity",
                                    "Building Placed by ${thisField.builtBy?.name}"
                                )
                                val playerIconColor =
                                    Color(thisField.builtBy?.color ?: Color.Black.toArgb())
                                canvas.save()
                                canvas.translate(
                                    hex.centerX - (iconSize / 2),
                                    hex.centerY - (iconSize / 2)
                                ) //Hälfte der Größe abziehen
                                houseIcon.apply {

                                    draw(
                                        size = Size(iconSize, iconSize),
                                        colorFilter = ColorFilter.tint(playerIconColor)
                                    )
                                }
                                canvas.restore()
                            }
                            //}
                        }
                    }
                    if (selectedQuadrant == null) {
                        var oldvLeft1: Offset = Offset(0f, 0f)
                        // Vertikale Grenze: Zeichne die Kanten der Hexagone in Spalte 9 (rechte Kante) und Spalte 10 (linke Kante)
                        for (row in 0 until overviewRows) {
                            // Rechte Kante des Hexagons in Spalte 9
                            val hexLeft = hexagons.first { it.row == row && it.col == 9 }
                            val vLeft1 = Offset(
                                hexLeft.centerX + overviewSide * cos(Math.toRadians(30.0)).toFloat(),
                                hexLeft.centerY + overviewSide * sin(Math.toRadians(30.0)).toFloat()
                            )
                            val vLeft2 = Offset(
                                hexLeft.centerX + overviewSide * cos(Math.toRadians(330.0)).toFloat(),
                                hexLeft.centerY + overviewSide * sin(Math.toRadians(330.0)).toFloat()
                            )

                            drawLine(
                                color = Color.Black,
                                start = vLeft1,
                                end = vLeft2,
                                strokeWidth = 8f
                            )
                            if (row > 0) {
                                drawLine(
                                    color = Color.Black,
                                    start = vLeft2,
                                    end = oldvLeft1,
                                    strokeWidth = 8f
                                )
                            }
                            oldvLeft1 = vLeft1


                        }
                        var oldvTop: Offset = Offset(0f, 0f)
                        // Horizontale Grenze: Zeichne die Kanten der Hexagone in Reihe 9 (untere Kante) und in Reihe 10 (obere Kante)
                        for (col in 0 until overviewCols) {
                            // Untere Kante des Hexagons in Reihe 9
                            val hexTop = hexagons.first { it.row == 9 && it.col == col }
                            val vTop = Offset(
                                hexTop.centerX + overviewSide * cos(Math.toRadians(90.0)).toFloat(),
                                hexTop.centerY + overviewSide * sin(Math.toRadians(90.0)).toFloat()
                            )
                            // Obere Kante des Hexagons in Reihe 10
                            val hexBottom = hexagons.first { it.row == 10 && it.col == col }
                            val vBottom = Offset(
                                hexBottom.centerX + overviewSide * cos(Math.toRadians(270.0)).toFloat(),
                                hexBottom.centerY + overviewSide * sin(Math.toRadians(270.0)).toFloat()
                            )
                            drawLine(
                                color = Color.Black,
                                start = vTop,
                                end = vBottom,
                                strokeWidth = 8f
                            )
                            if (col > 0) {
                                drawLine(
                                    color = Color.Black,
                                    start = vBottom,
                                    end = oldvTop,
                                    strokeWidth = 8f
                                )
                            }
                            oldvTop = vTop
                        }
                    }


                }
            }
        }
        val me = Player.localPlayer

        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            Column (
                modifier = Modifier
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ){
                Text("Mein Name: ${me.name}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Verbleibende Häuser: ${me.remainingSettlements}")
                Spacer(modifier = Modifier.height(10.dp))
                Text("Aktiver Spieler: ${activePlayer?.name ?: ""}")
            }
        }

        if (playerIsActive) {
            Box(modifier = Modifier.align(Alignment.BottomStart)) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    var str = terrainCardType ?: ""
                    if(me.id == activePlayer?.id) {
                        Row {
                            if (drawCardIsClicked && terrainCardType != null) {
                            Text("Card: $str")

                                Spacer(modifier = Modifier.width(8.dp)) // Abstand zum Text

                                val terrainColor = when (str) {
                                    "\"FOREST\"" -> colorResource(id = com.example.myapplication.R.color.terrain_forest)
                                    "\"GRASS\"" -> colorResource(id = com.example.myapplication.R.color.terrain_grass)
                                    "\"CANYON\"" -> colorResource(id = com.example.myapplication.R.color.terrain_canyon)
                                    "\"DESERT\"" -> colorResource(id = com.example.myapplication.R.color.terrain_desert)
                                    "\"FLOWERS\"" -> colorResource(id = com.example.myapplication.R.color.terrain_flowers)
                                    else -> Color.Transparent
                                }

                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(color = terrainColor, shape = RoundedCornerShape(4.dp))
                                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                )
                            }
                        }

                    }

                    Button(
                        onClick = {
                            onDrawCard(roomId)
                            drawCardIsClicked = true
                        },
                        enabled = !drawCardIsClicked,
                        modifier = Modifier
                            .padding(4.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = com.example.myapplication.R.color.beige_lobby_background),
                            contentColor = colorResource(id = com.example.myapplication.R.color.light_blue_900)
                        ),
                        shape = RoundedCornerShape(20.dp)) {
                        Text("Draw Card")
                    }
                    Button(
                        onClick = { MyStomp.toggleCheatMode(roomId);onToggleCheatMode },
                        modifier = Modifier.padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Farbe ändert sich, wenn der Modus aktiv ist
                            containerColor = if (isCheatModeActive) Color.Red else colorResource(id = com.example.myapplication.R.color.beige_lobby_background),
                            contentColor = if (isCheatModeActive) Color.White else colorResource(id = com.example.myapplication.R.color.light_blue_900)
                        )
                    ) {
                        Text(if (isCheatModeActive) "Cheat Mode: ON" else "Cheat Mode: OFF")
                    }

                    Button(
                        onClick = {
                            onEndTurn(roomId)
                            drawCardIsClicked = false
                            MyStomp.setPlayerActive(false)
                            //terrainCardType = null
                        },
                        enabled = drawCardIsClicked,
                        modifier = Modifier
                            .padding(8.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = com.example.myapplication.R.color.beige_lobby_background),
                            contentColor = colorResource(id = com.example.myapplication.R.color.light_blue_900)
                        ),
                        shape = RoundedCornerShape(20.dp)) {
                        Text("End Turn")
                    }
                }
            }
        }
        Box(modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp)) {
            Column(horizontalAlignment = Alignment.End) {
                // Der Melde-Button. Nur aktiv, wenn das Meldefenster offen ist.
                Button(
                    onClick = onReportPlayer,
                    enabled = isReportWindowActive,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.DarkGray
                    )
                ) {
                    Text("Melde Spieler!")
                }
            }
        }
    }
}

private fun GameViewModel.getLocalPlayer() {
    TODO("Not yet implemented")
}

class GameActivity : ComponentActivity(), SensorEventListener {

    private var terrainCardType by mutableStateOf<String?>(null)

    // Zustand für den Cheat-Modus
    private var isCheatModeActive by mutableStateOf(false)

    // Zustand, um zu wissen, ob tatsächlich geschummelt wurde
    private var hasPlacedCheatedHouse = mutableStateOf(false)

    // Zustand für das 3-Sekunden-Meldefenster
    private var isReportWindowActive by mutableStateOf(false)

    // ID des Spielers, der zuletzt am Zug war und gemeldet werden kann
    private var lastActivePlayerId = mutableStateOf<String?>(null)

    private val viewModel: GameViewModel by viewModels()
    private var activePlayer: PlayerDAO? = null
    private var players: List<PlayerDAO> = emptyList()

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH
    private val shakeThreshold = 15 // Schwellenwert für die Schüttelerkennung


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        viewModel.buildGameBoard()

        val roomId = intent.getStringExtra("ROOM_ID")

        val onDrawCard: (String) -> Unit = { roomId ->
            MyStomp.drawCard(roomId)
        }
        val onPlaceHouses: (String) -> Unit = { roomId ->
            MyStomp.placeHouses(roomId)

        }
        val onEndTurn: (String) -> Unit = { roomId ->
            Log.d(
                "GameActivity",
                "Attempting to end turn in room: ${roomId.orEmpty()}, has cheated: ${hasPlacedCheatedHouse.value}"
            )
            MyStomp.endTurn(roomId.orEmpty(), hasPlacedCheatedHouse.value)

            this@GameActivity.isCheatModeActive = false
            hasPlacedCheatedHouse.value = false
            terrainCardType = null
        }

        MyStomp.connect(context = this) {
            roomId?.let { validRoomId ->
                MyStomp.subscribeToGameUpdatesTerrainCard(validRoomId) { message ->
                    runOnUiThread { // wichtig!
                        viewModel.updateTerrainCardType(message)
                    }
                }
            }
        }

        MyStomp.connect(context = this) {
            roomId?.let { validRoomId ->
                MyStomp.subscribeToGameUpdates(validRoomId) { message ->
                    val obj = JSONObject(message)
                    val gameManager = obj.getJSONObject("gameManager")
                    activePlayer = PlayerDAO(
                        id = gameManager.getJSONObject("activePlayer").getString("id"),
                        name = gameManager.getJSONObject("activePlayer").getString("name"),
                        color = gameManager.getJSONObject("activePlayer").getInt("color"),
                        remainingSettlements = gameManager.getJSONObject("activePlayer").getInt("remainingSettlements"),
                        score = gameManager.getJSONObject("activePlayer").getInt("score")
                    )
                    val boardFields = gameManager.getJSONObject("gameBoard").getJSONArray("fields")
                    val playersJson = obj.getJSONArray("players")
                    val players = mutableListOf<PlayerDAO>()
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
                        if(playerObj.getString("id").equals(Player.localPlayer.id)){
                            Player.localPlayer.let{
                                it.remainingSettlements = playerObj.getInt("remainingSettlements")
                                it.score = playerObj.getInt("score")
                            }
                        }
                    }
                    MyStomp.setPlayerActive(activePlayer?.id == MyStomp.playerId)

                    // WICHTIG: immer im UI-Thread!
                    runOnUiThread {
                        viewModel.updatePlayers(players)
                        viewModel.updateGameBoardFromJson(boardFields, players)
                    }
                }

                roomId.let { validRoomId ->
                    MyStomp.subscribeToCheatReportWindow(validRoomId) { cheatWindowUpdate ->
                        this@GameActivity.isReportWindowActive = cheatWindowUpdate.isWindowActive
                        lastActivePlayerId.value = cheatWindowUpdate.reportedPlayerId

                        // Timer im Client starten, um den Button nach 3s wieder zu deaktivieren
                        if (cheatWindowUpdate.isWindowActive) {
                            // Coroutine nutzen, um nach 3 Sekunden den Zustand zurückzusetzen
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(3000)
                                this@GameActivity.isReportWindowActive = false
                            }
                        }
                    }
                }
            }
        }
        MyStomp.connect(context = this) {
            roomId?.let { validRoomId ->
                MyStomp.getGameUpdate(roomId)
                MyStomp.subscribeToScoreUpdates(roomId, context = this)
            }
        }

        setContent {
            HexagonBoardScreen(
                roomId = roomId.orEmpty(),
                onDrawCard = onDrawCard,
                onPlaceHouses = onPlaceHouses,
                onEndTurn = onEndTurn,
                terrainCardType = viewModel.terrainCardType,
                gameBoard = viewModel.gameBoard,
                players = viewModel.players,
                activePlayer = activePlayer,
                isCheatModeActive = isCheatModeActive,

                onToggleCheatMode = { isCheatModeActive = !isCheatModeActive } ,

                isReportWindowActive = isReportWindowActive,

                onHousePlaced = { hasCheated ->
                    if (hasCheated) {
                        hasPlacedCheatedHouse.value = true
                    }
                } ,

                onReportPlayer = {

                    Log.d("CHEAT_DEBUG", "Melde-Button geklickt. lastActivePlayerId ist: ${lastActivePlayerId.value}")

                    if (lastActivePlayerId.value != null) {
                        Log.d("CHEAT_DEBUG", "Bedingung erfüllt. Sende Meldung für Spieler: ${lastActivePlayerId.value!!}")
                        MyStomp.reportCheat(
                            roomId.orEmpty(),
                            MyStomp.playerId,
                            lastActivePlayerId.value!!
                        )
                        Toast.makeText(this, "Spieler gemeldet!", Toast.LENGTH_SHORT).show()
                        isReportWindowActive = false // Button sofort deaktivieren
                    } else {
                        Log.d("CHEAT_DEBUG", "Bedingung NICHT erfüllt. lastActivePlayerId war null. Meldung wird nicht gesendet.")
                    }
                }
            )
        }
    }

    // onResume und onPause für den Listener
    override fun onResume() {
        super.onResume()
        // Regristierte den Listener, wenn die Activity im Vordergrund ist
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause(){
        super.onPause()
        // Entferne den Listener, wenn die Activity nicht mehr im Vordergrund ist
        sensorManager.unregisterListener(this)
    }

    // Logik für die Sensoren
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > shakeThreshold) {
                // Prüfe, ob der aktuelle Spieler am Zug ist
                if (activePlayer?.id == MyStomp.playerId) {
                    val roomId = intent.getStringExtra("ROOM_ID")
                    if (roomId != null) {
                        runOnUiThread {
                            Toast.makeText(this, "Zug wird rückgängig gemacht!", Toast.LENGTH_SHORT).show()
                            MyStomp.undoLastMove(roomId)
                        }
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Wird für die Funktion nicht benötigt, muss aber implementiert werden.
    }
}
