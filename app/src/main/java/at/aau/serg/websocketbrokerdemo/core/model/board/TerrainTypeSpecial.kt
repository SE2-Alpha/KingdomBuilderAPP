package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Spezialgelände mit besonderen Eigenschaften.
 */

enum class TerrainTypeSpecial(override val isBuildable: Boolean = false) : TerrainType{
    WATER,   // Wasser (Verschiebeaktionen möglich)
    MOUNTAIN, // Berg (unpassierbar)
    SPECIALABILITY, //alle spezialfähigkeiten
    CITY; //alle städte

    fun allowsMovement() = this == WATER
}