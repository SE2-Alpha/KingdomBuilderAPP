package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField

/**
 * Repräsentiert das Königreich eines Spielers mit allen Siedlungen und Spezialplättchen.
 */

class Kingdom {
    /**
     * Liste aller Felder, auf denen der Spieler Siedlungen gebaut hat
     */
    private val settlements: MutableList<TerrainField> = mutableListOf()


    /**
     * @return Anzahl der gebauten Siedlungen
     */
    fun getSettlementCount() = settlements.size

    /**
     * Fügt eine neue Siedlung hinzu
     * @param field Das bebaute TerrainField
     */
    fun addSettlement(field: TerrainField) {
        settlements.add(field)
    }

    fun getAdjacentFields(board: GameBoard): List<TerrainField> {
        return settlements.flatMap  { board.getAdjacentFields(it) }
    }
}