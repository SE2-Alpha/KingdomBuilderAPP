package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.cards.Card

/**
 * Repräsentiert einen Spieler mit Königreich und Ressourcen.
 */

class Player(val id: String, val name: String, val color: Int) {
    /**
     * Verbleibende Siedlungen, die platziert werden können
     */
    var remainingSettlements: Int = 40
        private set
    val kingdom = Kingdom();
    private val handCards = mutableListOf<Card>()
    var score: Int = 0

    /**
     * @return True, wenn Bauaktion erfolgreich war
     * @param field Zielort für die Siedlung
     */
    fun buildSettlement(field: TerrainField): Boolean {
        TODO()
    }
    private fun canBuildSettlement(field: TerrainField): Boolean {
        return remainingSettlements > 0 &&
                field.isBuildable &&
                (kingdom.getSettlementCount() == 0)
    }
}