package at.aau.serg.kingdombuilder.core.model.board

/**
 * Basisklasse für alle Geländetypen.
 * @property isBuildable Kann auf diesem Feld gebaut werden?
 */

enum class TerrainType( val isBuildable: Boolean = true) {
    GRASS,
    CANYON,
    DESERT,
    FLOWERS,
    FOREST,
    WATER(false),
    MOUNTAIN(false),
    SPECIALABILITY(false), //special abilities
    CITY(false);

    /**
     * For the special ability
     */
    fun allowsMovement() = this == WATER
}