package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.Quadrant
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantFields
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantOasis
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantTavern
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantTower

import androidx.collection.emptyObjectList

/**
 * Das Hauptspielbrett mit allen Terrainfeldern.
 */

class GameBoard() {
    private val size = 400
    /**
     * 1D-Array der Felder [id]
     */
    private lateinit var fields: Array<TerrainField>

    /**
     * Builds the Gameboard. Implement with numbers later to decide what quadrants to pick
     */
    fun buildGameboard(){
        //merge all 4 quadrants into one
        val quadrant1 = fillQuadrant(1)
        val quadrant2 = fillQuadrant(2)
        val quadrant3 = fillQuadrant(3)
        val quadrant4 = fillQuadrant(4)

        //concat top two and bottom two quadrants together
        val concatTop = concatQuadrantFields(quadrant1, quadrant2)
        val concatBottom = concatQuadrantFields(quadrant3, quadrant4)

        val concat = concatTop + concatBottom
        fields = Array(400) {id -> TerrainField(concat[id]!!, id) }
    }

    /**
     * concats top/bottom quadrants together to it work easier in lines
     */
    fun concatQuadrantFields(quadrant1: Quadrant, quadrant2: Quadrant): Array<TerrainType?> {
        val concat = Array<TerrainType?>(200) {null}
        for(row in 0 .. 9){
            for(column in 0 .. 9){
                concat[20 * row + column] = quadrant1.getFieldType(10 * row + column)

                concat[20 * row + column + 10] = quadrant2.getFieldType(10 * row + column)
            }
        }
        return concat

    }

    /**
    * picks the quadrant based on number (1 - 12)
     */
    fun fillQuadrant(num: Int): Quadrant{
        return when(num){
            1 -> QuadrantTower()
            2 -> QuadrantFields()
            3 -> QuadrantOasis()
            4 -> QuadrantTavern()
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
    fun getFieldByRowAndCol(row: Int, col: Int): TerrainField {
        return fields[row*20 + col]
    }
}