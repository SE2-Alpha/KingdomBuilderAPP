package at.aau.serg.websocketbrokerdemo.core.actions

/**
 * Basisinterface für alle Spielaktionen (Bauen, Karten nutzen, etc.).
 */

interface Action {
    /**
     * Führt die Aktion aus
     * @return True bei Erfolg, False bei Fehler (z.B. ungültiger Zug)
     */
    fun execute(): Boolean

    /**
     * Macht die Aktion rückgängig
     * @return True bei Erfolg
     */
    fun undo(): Boolean
}