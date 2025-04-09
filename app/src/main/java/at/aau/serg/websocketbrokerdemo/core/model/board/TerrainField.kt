package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Einzelnes Feld auf dem Spielbrett.
 */

class TerrainField(var type: TerrainType, val x: Int, val y: Int) {
    /**
     * Referenz auf den Spieler, der hier gebaut hat (null wenn frei)
     */
    var builtBy: Player? = null

    var terrainType: TerrainType
        get() {
            return terrainType
        }
        set(type) {
            terrainType = (type)
        }
    /**
     * @return True, wenn das Feld aktuell bebaut werden kann
     */
    val isBuildable: Boolean
        get() = type.isBuildable && builtBy == null
}