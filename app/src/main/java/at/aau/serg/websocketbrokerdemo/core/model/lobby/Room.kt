package at.aau.serg.websocketbrokerdemo.core.model.lobby

data class Room(
    val id: String,
    val name: String,
    val size: Int,           // maximale Kapazität
    val currentUsers: Int,    // aktuell beigetretene Personen
    val status: RoomStatus // Status des Raums
)