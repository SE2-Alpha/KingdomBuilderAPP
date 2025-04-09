package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Standard-Geländetypen, auf denen gebaut werden kann.
 */

enum class TerrainTypeBuild(override val isBuildable: Boolean = true) : TerrainType {
    GRASS,  // Grasland
    CANYON, // Schlucht
    DESERT, // Wüste
    FLOWERS,// Blumenfeld
    FOREST  // Wald
}