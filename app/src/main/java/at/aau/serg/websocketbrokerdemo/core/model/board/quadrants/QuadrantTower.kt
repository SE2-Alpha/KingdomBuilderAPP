package at.aau.serg.websocketbrokerdemo.core.model.board.quadrants

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType

class QuadrantTower(): Quadrant {
    override fun getFieldType(id: Int): TerrainType {
        return when(id){
            0, 1, 2, 3, 10, 12, 13, 22, 85, 86, 94, 95, 96 -> TerrainType.FOREST
            4, 5, 7, 11, 16, 17, 18, 29, 38, 39 -> TerrainType.MOUNTAIN
            6, 15, 26, 27, 36, 46, 58, 68, 78, 79, 88, 89, 97, 98, 99 -> TerrainType.GRASS
            8, 9, 19, 48, 49, 51, 57, 59, 62, 69, 70, 71, 81, 91, 92 -> TerrainType.CANYON
            14, 20, 21, 23, 24, 25, 31, 32, 33, 44, 66, 75, 76, 77, 87 -> TerrainType.FLOWERS
            28, 34, 37, 45, 47, 55, 56, 65, 74, 82, 83, 84, 93 -> TerrainType.WATER
            30, 40, 41, 42, 43, 50, 52, 53, 54, 60, 61, 63, 64, 73, 80, 90 -> TerrainType.DESERT
            35, 72 -> TerrainType.SPECIALABILITY
            67 -> TerrainType.CITY
            else -> TerrainType.DESERT
        }
    }



}