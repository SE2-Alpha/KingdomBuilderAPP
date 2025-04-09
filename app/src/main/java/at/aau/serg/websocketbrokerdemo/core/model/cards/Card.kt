package at.aau.serg.websocketbrokerdemo.core.model.cards

/**
 * Basisinterface für alle Karten im Spiel.
 */

interface Card {
    /**
     * Eindeutige Karten-ID für Serialisierung
     */
    val id: String

    /**
     * Anzeigename im UI
     */
    val name: String
}