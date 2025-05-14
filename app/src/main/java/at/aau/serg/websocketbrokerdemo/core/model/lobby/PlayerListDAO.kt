package at.aau.serg.websocketbrokerdemo.core.model.lobby

import at.aau.serg.websocketbrokerdemo.core.model.player.PlayerDAO

/**
 * Matching Data Access Class to receive StartRoomMessage
 */
data class PlayerListDAO(val roomId:String, val players: List<PlayerDAO>)