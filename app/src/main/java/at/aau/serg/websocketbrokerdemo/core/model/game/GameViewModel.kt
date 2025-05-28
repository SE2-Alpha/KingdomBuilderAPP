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
    var gameBoard by mutableStateOf(GameBoard())
        private set

    fun buildGameBoard() {
        gameBoard = GameBoard().apply { buildGameboard() }
    }

    // Callback zum Updaten des Boards aus JSON
    fun updateGameBoardFromJson(boardFields: org.json.JSONArray, players: List<PlayerDAO>) {
        val newBoard = GameBoard()
        newBoard.buildGameboard()
        for (j in 0 until boardFields.length()) {
            val field = boardFields.getJSONObject(j)
            System.out.println("Updating field: "+field);
            newBoard.setField(
                id = field.getInt("id"),
                type = TerrainType.valueOf(field.getString("type")),
                builtBy = players.firstOrNull { it.id == field.getString("owner") } as Player?,
                ownerSinceRound = field.getInt("ownerSinceRound")
            )
        }
        gameBoard = newBoard
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
