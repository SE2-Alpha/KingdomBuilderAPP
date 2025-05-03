package at.aau.serg.websocketbrokerdemo.core.actions

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Bauaktion einer Siedlung.
 * @property player Der ausführende Spieler
 * @property field Zielort der Bebauung
 */

class BuildAction(private val player: Player, private val field: TerrainField, private val gameBoard: GameBoard) : Action {
    /**
     * @throws IllegalStateException Wenn Feld nicht bebaubar ist
     */

    private var wasExecuted = false

    override fun execute(): Boolean {
        if(wasExecuted || !player.validateBuild(field)) return false

        wasExecuted = player.buildSettlement((field))
        return wasExecuted
    }

    override fun undo(): Boolean {
        if (!wasExecuted) return false
        wasExecuted = !player.undoBuildSettlement(field)
        return !wasExecuted
    }
}