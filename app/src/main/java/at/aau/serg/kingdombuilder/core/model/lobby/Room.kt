package at.aau.serg.kingdombuilder.core.model.lobby

import at.aau.serg.kingdombuilder.core.model.player.PlayerDAO

data class Room(
    val id: String,
    val name: String,
    val size: Int,           // maximale Kapazität
    val currentUsers: Int,    // aktuell beigetretene Personen
    val status: RoomStatus, // Status des Raums
    val players: List<PlayerDAO>, // Liste der Spieler im Raum
)