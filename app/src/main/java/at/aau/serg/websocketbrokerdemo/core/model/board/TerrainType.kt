package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Basisklasse für alle Geländetypen.
 * @property isBuildable Kann auf diesem Feld gebaut werden?
 */

sealed interface TerrainType{
    val isBuildable: Boolean
}