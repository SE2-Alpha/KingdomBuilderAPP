package at.aau.serg.websocketbrokerdemo.core.model.cards

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainTypeBuild

/**
 * Geländekarte, die Bauorte vorgibt.
 * @property terrainType Auf diesem Terrain muss gebaut werden
 */

class TerrainCard (override val id: String, override val name: String, val terrainType: TerrainTypeBuild) : Card