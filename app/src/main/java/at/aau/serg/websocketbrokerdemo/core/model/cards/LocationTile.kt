package at.aau.serg.websocketbrokerdemo.core.model.cards

import at.aau.serg.websocketbrokerdemo.core.actions.SpecialAction

/**
 * Ortsplättchen mit Sonderfähigkeiten.
 * @property specialAction Ausführbare Spezialaktion
 */

class LocationTile (override val id: String, override val name: String, val specialAction: SpecialAction) : Card