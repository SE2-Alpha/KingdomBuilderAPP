package at.aau.serg.websocketbrokerdemo.core.model.board.quadrants

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType

class QuadrantFields(): Quadrant {
    override fun getFieldType(id: Int): TerrainType {
        return when(id){
            5, 6, 7, 14, 15, 16, 26, 63, 72, 73, 82, 83, 90, 91, 92 -> TerrainType.FOREST
            74, 81 -> TerrainType.MOUNTAIN
            8, 9, 18, 19, 41, 42, 50, 51, 60, 61, 62, 70, 71, 80 -> TerrainType.GRASS
            2, 12, 20, 21, 22, 27, 30, 31, 37, 38, 40, 48, 49, 59 -> TerrainType.CANYON
            23, 24, 25, 28, 29, 32, 33, 39, 44, 45, 53, 55, 63, 65 -> TerrainType.FLOWERS
            3, 4, 13, 34, 43, 54, 56, 66, 67, 75, 76, 77, 79, 84, 85, 86, 87, 88, 89, 93, 94, 95, 96, 97, 98, 99 -> TerrainType.WATER
            0, 1, 10, 35, 36, 46, 47, 57, 58, 68, 69, 78 -> TerrainType.DESERT
            17, 52 -> TerrainType.SPECIALABILITY
            11 -> TerrainType.CITY
            else -> TerrainType.WATER
        }
    }



}