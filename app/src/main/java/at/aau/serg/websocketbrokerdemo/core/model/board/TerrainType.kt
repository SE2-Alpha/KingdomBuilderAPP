package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Basisklasse für alle Geländetypen.
 * @property isBuildable Kann auf diesem Feld gebaut werden?
 */

enum class TerrainType( val isBuildable: Boolean = true) {
    GRASS,  // Grasland
    CANYON, // Schlucht
    DESERT, // Wüste
    FLOWERS,// Blumenfeld
    FOREST,  // Wald
    WATER(false),   // Wasser (Verschiebeaktionen möglich)
    MOUNTAIN(false), // Berg (unpassierbar)
    SPECIALABILITY(false), //alle Spezialfähigkeiten
    CITY(false); //alle Städte

    /**
     * For the special ability
     */
    fun allowsMovement() = this == WATER
}