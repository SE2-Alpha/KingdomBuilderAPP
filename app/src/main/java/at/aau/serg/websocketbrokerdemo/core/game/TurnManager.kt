package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Verwaltet Spielzüge und Rundenzählung.
 */

class TurnManager(private val players: List<Player>) {
    /**
     * Aktuelle Rundennummer (beginnend bei 1)
     */
    var currentRound: Int = 0
        private set

    /**
     * Beendet den Zug des aktuellen Spielers:
     * - Wechselt zum nächsten Spieler
     * - Erhöht die Runde, wenn alle dran waren
     */
    fun endTurn() {
        TODO()
    }
}