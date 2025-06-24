package at.aau.serg.kingdombuilder.core.model.board.quadrants

import at.aau.serg.kingdombuilder.core.model.board.TerrainType

interface Quadrant {
    fun getFieldType(id: Int): TerrainType

}