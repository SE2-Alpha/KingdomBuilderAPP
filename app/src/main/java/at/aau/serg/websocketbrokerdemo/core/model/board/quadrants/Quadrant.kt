package at.aau.serg.websocketbrokerdemo.core.model.board.quadrants

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType

interface Quadrant {
    fun getFieldType(id: Int): TerrainType

}