package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Einzelnes Feld auf dem Spielbrett.
 */

class TerrainField(val type: TerrainType, val id: Int) { //needs quadrant number and ids of neighbours
    /**
     * Referenz auf den Spieler, der hier gebaut hat (null wenn frei)
     */

    var builtBy: Player? = null

    /**
     * @return True, wenn das Feld aktuell bebaut werden kann
     */
    val isBuildable: Boolean
        get() = type.isBuildable && builtBy == null
}