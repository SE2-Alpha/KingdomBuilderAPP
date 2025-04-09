package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.cards.LocationTile

/**
 * Repräsentiert das Königreich eines Spielers mit allen Siedlungen und Spezialplättchen.
 */

class Kingdom {
    /**
     * Liste aller Felder, auf denen der Spieler Siedlungen gebaut hat
     */
    private val settlements: MutableList<TerrainField> = mutableListOf()

    /**
     * Aktive Sonderfähigkeiten (Orakel, Turm etc.) des Königreichs
     */
    private val specialTiles: MutableList<LocationTile> = mutableListOf()

    /**
     * @return Anzahl der gebauten Siedlungen
     */
    fun getSettlementCount(): Int {
        TODO()
    }

    /**
     * Fügt eine neue Siedlung hinzu
     * @param field Das bebaute TerrainField
     */
    fun addSettlement(field: TerrainField) {
        TODO()
    }
}