package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Verwaltet Spielzüge und Rundenzählung.
 */

class TurnManager(private val players: List<Player>) {
    /**
     * Aktuelle Rundennummer (beginnend bei 1)
     */
    private var currentPlayerIndex = 0
    var currentRound: Int = 0
        private set

    val currentPlayer: Player
        get() = players[currentPlayerIndex]

    /**
     * Beendet den Zug des aktuellen Spielers:
     * - Wechselt zum nächsten Spieler
     * - Erhöht die Runde, wenn alle dran waren
     */
    fun endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        if (currentPlayerIndex == 0) currentRound++
    }

    fun isGameOver(): Boolean{
        return players.any { it.remainingSettlements == 0 }
    }
}