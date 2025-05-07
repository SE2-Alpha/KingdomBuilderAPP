package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.Quadrant
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantFields
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantOasis
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantTavern
import at.aau.serg.websocketbrokerdemo.core.model.board.quadrants.QuadrantTower
import kotlin.math.abs

import androidx.collection.emptyObjectList
import at.aau.serg.websocketbrokerdemo.core.model.player.Kingdom

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
            2 -> QuadrantTavern()
            3 -> QuadrantOasis()
            4 -> QuadrantFields()
            else -> throw IllegalArgumentException("Unknown Quadrant: $num")

        }
    }

    /**
     * @return Alle Felder eines bestimmten TerrainTyps (z.B. alle Wälder)
     * @param type Gesuchter TerrainType
     */
    fun getFieldsByType(type: TerrainType): List<TerrainField> {
        return fields.filter {it.type == type}
    }

    /**
     * Prüft, ob zwei Felder benachbart sind
     * @param field1 Erstes Feld
     * @param field2 Zweites Feld
     */
    fun areFieldsAdjacent(field1: TerrainField, field2: TerrainField): Boolean {
        val pos1 = field1.id
        val pos2 = field2.id
        val row1 = pos1 / 20
        val row2 = pos2 / 20
        
        return when{
            row1 == row2 -> abs(pos1 - pos2) == 1
            abs(row1 - row2) == 1 -> abs(pos1 - pos2 - row1 + row2) in 9..11
            else -> false
        }
    }
    fun getFieldByRowAndCol(row: Int, col: Int): TerrainField {
        return fields[row*20 + col]
    }
    fun getAdjacentFields(field: TerrainField): List<TerrainField> {
        val adjacent = mutableListOf<TerrainField>()
        val position = field.id
        //Hex-Nachbarschaftslogik
        val offsets = listOf(-10, -9, 1, 11, 10, 9)
        offsets.forEach { offset ->
            fields.getOrNull(position + offset)?.let { adjacent.add(it)}
        }
        return adjacent
    }
    fun areFieldAdjacentToKingdom(field: TerrainField, kingdom: Kingdom): Boolean {
        return kingdom.getAdjacentFields(this).any { adjacent ->
            areFieldsAdjacent(field,adjacent)
        }
    }
}