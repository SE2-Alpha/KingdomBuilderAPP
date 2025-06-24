package at.aau.serg.kingdombuilder.core.model.board.quadrants

import at.aau.serg.kingdombuilder.core.model.board.TerrainType

class QuadrantTavern(): Quadrant {
    override fun getFieldType(id: Int): TerrainType {
        return when(id){
            0, 10, 11, 20, 21, 22, 23, 24, 25, 26, 32, 40, 41, 50, 61 -> TerrainType.FLOWERS
            1, 2, 5, 6, 12, 13, 14, 60, 70, 71, 80, 81, 82, 90, 91 -> TerrainType.DESERT
            3, 4, 15, 16, 27, 28, 29, 38, 39 -> TerrainType.MOUNTAIN
            7, 8, 9, 17, 18, 19, 49, 51, 52, 57, 58, 59, 63, 68, 72 -> TerrainType.CANYON
            30, 31, 42, 43, 53, 64, 73, 83, 92, 93 -> TerrainType.WATER
            33 -> TerrainType.CITY
            34, 35, 44, 45, 46, 54, 69, 76, 77, 78, 79, 87, 88, 89, 97, 98, 99 -> TerrainType.GRASS
            36, 37, 47, 48, 55, 56, 65, 66, 74, 75, 84, 85, 86, 94, 95, 96 -> TerrainType.FOREST
            62, 67 -> TerrainType.SPECIALABILITY
            else -> TerrainType.FOREST
        }
    }



}