package at.aau.serg.websocketbrokerdemo.core.game

import android.app.GameState
import android.os.Build
import androidx.annotation.RequiresApi
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Zentrale Steuernungsklasse für den Spielablauf.
 * Verwaltet Spielinitialisierung, Rundenlogik und den globalen Spielzustand.
 */

class GameManager(private val players: List<Player>, private  val turnManager: TurnManager) {
    /**
     * Aktives Spielbrett mit Terrainfeldern
     */
    private lateinit var gameBoard: GameBoard

    /**
     * Initialisiert das Spiel:
     * - Erstellt das Spielbrett
     * - Mischt die Kartenstapel
     * - Setzt Startwerte für Spieler
     */
    fun initializeGame() {
        TODO()
    }

    /**
     * @return Aktueller Spielzustand (Brett, Spielerstände, Karten)
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getCurrentGameState(): GameState{
        TODO()
    }
}

