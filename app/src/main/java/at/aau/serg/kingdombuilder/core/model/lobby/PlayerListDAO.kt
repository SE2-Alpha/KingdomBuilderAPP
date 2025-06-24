package at.aau.serg.kingdombuilder.core.model.lobby

import at.aau.serg.kingdombuilder.core.model.player.PlayerDAO

/**
 * Matching Data Access Class to receive StartRoomMessage
 */
data class PlayerListDAO(val roomId:String, val players: List<PlayerDAO>)