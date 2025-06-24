package at.aau.serg.kingdombuilder.core.model.player

/**
 * Matching Data Access Class to store PlayerList when receiving startRoomMessage
 */
data class PlayerDAO(val id:String, val name:String, val color:Int, val remainingSettlements:Int, val score:Int) {
    companion object
}
