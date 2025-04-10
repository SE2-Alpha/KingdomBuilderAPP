package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField

/**
 * Repräsentiert einen Spieler mit Königreich und Ressourcen.
 */

class Player(val id: String, val name: String, val color: Int) {
    /**
     * Verbleibende Siedlungen, die platziert werden können
     */
    var remainingSettlements: Int = 40
        private set

    /**
     * @return True, wenn Bauaktion erfolgreich war
     * @param field Zielort für die Siedlung
     */
    fun buildSettlement(field: TerrainField): Boolean {
        TODO()
    }
}