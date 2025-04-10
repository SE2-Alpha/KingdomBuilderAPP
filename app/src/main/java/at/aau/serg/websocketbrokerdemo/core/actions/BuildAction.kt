package at.aau.serg.websocketbrokerdemo.core.actions

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Bauaktion einer Siedlung.
 * @property player Der ausführende Spieler
 * @property field Zielort der Bebauung
 */

abstract class BuildAction(private val player: Player, private val field: TerrainField) : Action {
    /**
     * @throws IllegalStateException Wenn Feld nicht bebaubar ist
     */
    override fun execute(): Boolean {
        TODO()
    }
}