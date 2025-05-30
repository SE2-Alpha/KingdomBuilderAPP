package at.aau.serg.websocketbrokerdemo.core.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerDAO
import kotlin.collections.mutableListOf

class GameViewModel : ViewModel() {

    // Board als Compose-State
    var gameBoard = GameBoard()

    fun buildGameBoard() {
        gameBoard = GameBoard().apply { buildGameboard() }
    }

    // Callback zum Updaten des Boards aus JSON
    fun updateGameBoardFromJson(fields: org.json.JSONArray, players: List<PlayerDAO>) {
        // Board komplett neu bauen und setzen (Compose merkt das!)
        gameBoard = GameBoard().apply { updateGameBoardFromJson(fields, players) }
    }

    var terrainCardType by mutableStateOf<String?>(null)
        private set

    fun updateTerrainCardType(message: String) {
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

    fun resetTerrainCardType() {
        terrainCardType = null
    }
}
