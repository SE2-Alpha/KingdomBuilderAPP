package at.aau.serg.websocketbrokerdemo.core.actions

import androidx.annotation.VisibleForTesting
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

class MoveAction(private val player: Player, private val fromField: TerrainField, private val toField: TerrainField) : Action{
    /**
     * Führt die Verschiebung durch:
     * 1. Entfernt Siedlung vom Ursprungsfeld
     * 2. Baut Siedlung auf Zielfeld
     *
     * @return True wenn Verschiebung erfolgreich war
     * @throws IllegalStateException Wenn:
     *   - Ursprungsfeld nicht dem Spieler gehört
     *   - Zielfeld nicht bebaubar ist
     *   - Keine Verbindung zum Königreich besteht
     */
    override fun execute(): Boolean {
        if (!isValidMove()) return false

        // Entferne Siedlung vom Ursprungsfeld
        fromField.builtBy = null
        player.kingdom.addSettlement(toField)
        toField.builtBy = player
        return true
    }

    /**
     * Macht die Verschiebung rückgängig:
     * 1. Baut Siedlung zurück auf Ursprungsfeld
     * 2. Entfernt Siedlung vom Zielfeld
     *
     * @return True wenn Rückgängigmachen erfolgreich war
     * @throws IllegalStateException Wenn Zielfeld inzwischen blockiert ist
     */
    override fun undo(): Boolean {
        // Setze Siedlung zurück
        toField.builtBy = null
        fromField.builtBy = player
        return true
    }

    /**
     * Hilfsmethode zur Validierung von Verschiebungsregeln
     * @return True wenn:
     *   - Beide Felder existieren
     *   - Entfernung ≤ 2 Felder (je nach Spielvariante)
     *   - Geländetyp des Zielfelds erlaubt ist
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isValidMove(): Boolean {
        /*TODO()*/
        return true
    }
}