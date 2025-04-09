package at.aau.serg.websocketbrokerdemo.core.model.board

/**
 * Das Hauptspielbrett mit allen Terrainfeldern.
 */

class GameBoard() {
    private val qsize = 100
    private val gameBoardSize = 400
    /**
     * 2D-Array der Felder [Reihe][Spalte]
     */
    fun buildGameboard(){
        //merge all 4 quadrants into one
        var i = 0
        while(i < 4){

            i++
        }
    }
    private val fields: Array<TerrainField> = Array(qsize) {
        TODO()

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