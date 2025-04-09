package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.Quadrant
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantTower

/**
 * Das Hauptspielbrett mit allen Terrainfeldern.
 */

class GameBoard() {
    private val size = 400
    /**
     * 1D-Array der Felder [id]
     */
    private lateinit var fields: Array<TerrainField>

    fun buildGameboard(){
        //merge all 4 quadrants into one
        val quadrant1 = fillQuadrant(1)
        val quadrant2 = fillQuadrant(2)
        val quadrant3 = fillQuadrant(3)
        val quadrant4 = fillQuadrant(4)

        val concatTop = concatQuadrantFields(quadrant1, quadrant2)
        val concatBottom = concatQuadrantFields(quadrant3, quadrant4)
        //gameboard = quadrant1 + quadrant2 + quadrant3 + quadrant4

    }

    fun concatQuadrantFields(quadrant1: Quadrant, quadrant2: Quadrant) {
        val concat = Array<TerrainType?>(200) {null}
        for(row in 0 .. 9){
            for(column in 0 .. 9){
                concat[20 * row + column] = quadrant1.getFieldType(10 * row + column)
            }
        }
    }

    fun fillQuadrant(num: Int): Quadrant{
        return when(num){
            1 -> QuadrantTower()
            else -> throw IllegalArgumentException("Unknown Quadrant: $num")

        }
    }

    /**
     * @return Alle Felder eines bestimmten TerrainTyps (z.B. alle Wälder)
     * @param type Gesuchter TerrainType
     */
    fun getFieldsByType(type: TerrainType): List<TerrainField> {
        TODO()
    }

    /**
     * Prüft, ob zwei Felder benachbart sind
     * @param field1 Erstes Feld
     * @param field2 Zweites Feld
     */
    fun areFieldsAdjacent(field1: TerrainField, field2: TerrainField): Boolean {
        TODO()
    }
}