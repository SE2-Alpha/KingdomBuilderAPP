package at.aau.serg.websocketbrokerdemo.core.model.board

import androidx.collection.emptyObjectList

/**
 * Das Hauptspielbrett mit allen Terrainfeldern.
 */

class GameBoard(private val size: Int) {
    /**
     * 2D-Array der Felder [Reihe][Spalte]
     */
    private val fields: Array<Array<TerrainField>> = Array(size) {
        /*TODO()*/
        Array(size){ TerrainField(TerrainTypeBuild.GRASS, it, it) }
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