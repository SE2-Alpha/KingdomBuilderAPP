package at.aau.serg.websocketbrokerdemo.core.model.cards

/**
 * Siegpunktekarte mit speziellen Bedingungen.
 * @property victoryPoints Punkte bei Erfüllung
 */

class KingdomBuilderCard (override  val id: String, override val name: String, val victoryPoints: Int) : Card