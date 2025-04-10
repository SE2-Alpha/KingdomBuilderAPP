package at.aau.serg.websocketbrokerdemo.core.model.board.quadrants

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType

class QuadrantOasis(): Quadrant {
    override fun getFieldType(id: Int): TerrainType {
        return when(id){
            3, 4, 7, 8, 9, 14, 17, 18, 19, 24, 25, 29, 34, 38, 39 -> TerrainType.FOREST
            50, 51, 63, 74, 87 -> TerrainType.MOUNTAIN
            0, 1, 2, 6, 10, 11, 12, 16, 20, 23, 27, 28, 33, 53, 54, 64 -> TerrainType.GRASS
            32, 43, 44, 52, 60, 61, 62, 70, 71, 78, 79, 88, 89, 99 -> TerrainType.CANYON
            21, 22, 30, 31, 36, 40, 41, 42, 46, 65, 66, 67, 76, 77 -> TerrainType.FLOWERS
            5, 15, 26, 35, 45, 48, 49, 55, 56, 57, 80, 81, 82, 90, 91, 92, 93, -> TerrainType.WATER
            58, 59, 68, 69, 73, 75, 83, 84, 85, 86, 94, 95, 96, 97, 98 -> TerrainType.DESERT
            47 -> TerrainType.SPECIALABILITY
            13, 72 -> TerrainType.CITY
            else -> TerrainType.WATER
        }
    }



}