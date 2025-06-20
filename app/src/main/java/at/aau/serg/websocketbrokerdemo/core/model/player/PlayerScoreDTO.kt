package at.aau.serg.websocketbrokerdemo.core.model.player

data class PlayerScoreDTO(
    val playerId: String,
    val playerName: String,
    val points: Int
)