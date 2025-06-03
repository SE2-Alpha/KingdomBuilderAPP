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

// Im ViewModel
class GameViewModel : ViewModel() {
    var gameBoard by mutableStateOf(GameBoard())
        private set

    var terrainCardType by mutableStateOf<String?>(null)
        private set

    // NEU: updateGameBoardFromJson ersetzt das Board durch ein neues Objekt (falls notwendig)
    fun updateGameBoardFromJson(boardFields: org.json.JSONArray, players: List<PlayerDAO>) {
        val newGameBoard = GameBoard()
        newGameBoard.updateGameBoardFromJson(boardFields, players)
        gameBoard = newGameBoard // Triggert Compose-Redraw!
    }

    fun buildGameBoard() {
        gameBoard.buildGameboard()
    }

    fun updateTerrainCardType(message: String) {
        terrainCardType = message
    }
    var players by mutableStateOf<List<PlayerDAO>>(emptyList())
        private set

    fun updatePlayers(newPlayers: List<PlayerDAO>) {
        players = newPlayers
    }
}

