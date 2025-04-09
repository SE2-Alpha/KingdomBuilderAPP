package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Das Hauptspielbrett mit allen Terrainfeldern.
 */

class GameBoard() {
    private val qsize = 100
    /**
     * 2D-Array der Felder [Reihe][Spalte]
     */

    var gameboard = null
    fun buildGameboard(){
        //merge all 4 quadrants into one
        val quadrant1 = fillQuadrant(1)
        val quadrant2 = fillQuadrant(2)
        val quadrant3 = fillQuadrant(3)
        val quadrant4 = fillQuadrant(4)

        gameboard = quadrant1 + quadrant2 + quadrant3 + quadrant4
    }
    private val fields: Array<TerrainField> = Array(qsize) {
        TODO()

    }

    fun fillQuadrant(val num){
        when(num){
            1 -> return
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