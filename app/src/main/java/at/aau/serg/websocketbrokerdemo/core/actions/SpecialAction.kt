package at.aau.serg.websocketbrokerdemo.core.actions

import at.aau.serg.websocketbrokerdemo.core.model.cards.LocationTile
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Ausführung einer Ortsplättchen-Sonderfähigkeit.
 */

abstract class SpecialAction(private val player: Player, private val locationTile: LocationTile) : Action {
    /**
     * @return True, wenn Aktion gültig war und ausgeführt wurde
     */
    override fun execute(): Boolean {
        TODO()
    }
}