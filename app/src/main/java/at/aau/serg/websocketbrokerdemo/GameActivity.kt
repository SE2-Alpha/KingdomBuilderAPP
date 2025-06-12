package at.aau.serg.websocketbrokerdemo

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
    val markedFields = remember { mutableStateMapOf<Triple<String, Int, Int>, Boolean>() }
    val gameBoard = remember { GameBoard() }
    var houseIcon = rememberVectorPainter(Icons.Rounded.Home)
    gameBoard.buildGameboard()

    val playerIsActive by remember { derivedStateOf { MyStomp.playerIsActive } }
    var drawCardIsClicked by remember { mutableStateOf(false) }


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
                    Text("Zurück")
                }
            }
            // Canvas zum Zeichnen und Erfassen von Klicks
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(selectedQuadrant) {
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
                                        val key = Triple(selectedQuadrant!!, localRow, localCol)
                                        val currentlyMarked = markedFields[key] == true
                                        //TODO(): Make building of completed turns permanent

                                        // Prüfen, ob normal oder via Cheat platziert werden darf
                                        val canPlaceNormally = hex.field.isBuildable
                                        val canPlaceWithCheat = isCheatModeActive

                                        if (!currentlyMarked && (canPlaceNormally || canPlaceWithCheat)) {
                                            // Feld makieren und Haus platzieren
                                            markedFields[key] = true
                                            hex.field.builtBy = Player.localPlayer
                                            gameBoard.getFieldByRowAndCol(
                                                hex.row,
                                                hex.col
                                            ).builtBy = Player.localPlayer

                                            // Der Activity melden, ob dieaer Zug ein Cheat war
                                            val wasCheated = isCheatModeActive
                                            Log.d(
                                                "CHEAT_DEBUG",
                                                "HAUS PLATZIERT: isCheatModeActive ist ($isCheatModeActive), wasCheated ist ($wasCheated)"
                                            )
                                            onHousePlaced(wasCheated)

                                            Log.i(
                                                "Player Interaction",
                                                "Field ${hex.row}, ${hex.col} placed. Was cheated: $wasCheated"
                                            )

                                        } else if (currentlyMarked) {
                                            // Optional: Erlaube das Entfernen von Häusern in der gleichen Runde
                                            markedFields[key] = false
                                            hex.field.builtBy = null
                                            gameBoard.getFieldByRowAndCol(
                                                hex.row,
                                                hex.col
                                            ).builtBy = null
                                            Log.i(
                                                "Player Interaction",
                                                "Field ${hex.row}, ${hex.col} removed."
                                            )
                                        }
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
                        val localRow = hex.row - rowOffset
                        val localCol = hex.col - colOffset
                        val key = Triple(hex.quadrant, localRow, localCol)
                        val fillColor = hex.field.getColor(context) // Hexagon-Farbe
                        // Zuerst Füllung, dann Kontur zeichnen
                        drawPath(path = hexPath, color = fillColor)
                        drawPath(path = hexPath, color = Color.Black, style = Stroke(width = 2f))
                        //Falls Feld besetzt, Gebäude Zeichnen
                        if (markedFields[key] == true || hex.field.builtBy != null) {
                            if (drawCardIsClicked) {
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
                            }
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

        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            Column {
                Text(MyStomp.playerId)

                Button(
                    onClick = { MyStomp.setPlayerActive(true) },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Activate Player")
                }
                Button(
                    onClick = { MyStomp.setPlayerActive(false) },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("Deactivate Player")
                }
            }
        }

        // Rechter unterer Bereich: Melde-Button
        Box(modifier = Modifier
            .align(Alignment.TopStart)
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

            if (playerIsActive) {
                Box(modifier = Modifier.align(Alignment.BottomStart)) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        terrainCardType?.let {
                            Text("Terraintype: $it")
                        }
                        Button(
                            onClick = {
                                onDrawCard(roomId)
                                drawCardIsClicked = true
                            },
                            enabled = !drawCardIsClicked,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Draw Card")
                        }
                        Button(
                            onClick = onToggleCheatMode,
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                // Farbe ändert sich, wenn der Modus aktiv ist
                                containerColor = if (isCheatModeActive) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(if (isCheatModeActive) "Cheat Mode: ON" else "Cheat Mode: OFF")
                        }


                        Button(
                            onClick = { onPlaceHouses(roomId) },
                            enabled = drawCardIsClicked,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Place Houses")
                        }
                        Button(
                            onClick = {
                                onEndTurn(roomId)
                                drawCardIsClicked = false
                                MyStomp.setPlayerActive(false)
                               // terrainCardType = null
                            },
                            enabled = drawCardIsClicked,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("End Turn")
                        }
                    }
                }
            }
    }
}

class GameActivity : ComponentActivity() {

    private var terrainCardType by mutableStateOf<String?>(null)

    // Zustand für den Cheat-Modus
    private var isCheatModeActive by mutableStateOf(false)

    // Zustand, um zu wissen, ob tatsächlich geschummelt wurde
    private var hasPlacedCheatedHouse = mutableStateOf(false)

    // Zustand für das 3-Sekunden-Meldefenster
    private var isReportWindowActive by mutableStateOf(false)

    // ID des Spielers, der zuletzt am Zug war und gemeldet werden kann
    private var lastActivePlayerId = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getStringExtra("ROOM_ID")

        val onDrawCard: (String) -> Unit = { roomId ->
            Log.d("GameActivity", "Attempting to draw card in room: $roomId")
            MyStomp.drawCard(roomId)
        }

        val onPlaceHouses: (String) -> Unit = { roomId ->
            Log.d("GameActivity", "Attempting to place houses in room: $roomId")
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

        roomId?.let { validRoomId ->
            MyStomp.subscribeToGameUpdatesTerrainCard(validRoomId) { message ->
                Log.d("GameActivity", "Terrain Card: $message")

                val terrainType = when (message.toIntOrNull()) {
                    0 -> TerrainType.GRASS
                    1 -> TerrainType.CANYON
                    2 -> TerrainType.DESERT
                    3 -> TerrainType.FLOWERS
                    4 -> TerrainType.FOREST
                    else -> null
                }

                terrainCardType = terrainType?.toString()

            }
        } ?: Log.e("GameActivity", "Room ID is null. Cannot subscribe to game updates.")

        roomId?.let { validRoomId ->
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

        // In GameActivity.kt

        setContent {
            HexagonBoardScreen(
                roomId = roomId.orEmpty(),
                onDrawCard = onDrawCard,
                onPlaceHouses = onPlaceHouses,
                onEndTurn = onEndTurn,
                terrainCardType = terrainCardType,
                isCheatModeActive = isCheatModeActive,

                onToggleCheatMode = remember { { isCheatModeActive = !isCheatModeActive } },

                isReportWindowActive = isReportWindowActive,

                onHousePlaced = remember { { isCheated ->
                    if (isCheated) {
                        hasPlacedCheatedHouse.value = true
                    }
                } },

                onReportPlayer = remember { {
                    if (lastActivePlayerId.value != null) {
                        MyStomp.reportCheat(
                            roomId.orEmpty(),
                            MyStomp.playerId,
                            lastActivePlayerId.value!!
                        )
                        Toast.makeText(this, "Spieler gemeldet!", Toast.LENGTH_SHORT).show()
                        isReportWindowActive = false // Button sofort deaktivieren
                    }
                } }
            )
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun HexagonBoardScreenPreview() {
   HexagonBoardScreen()
}


 */