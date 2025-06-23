package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.cards.Card
import at.aau.serg.websocketbrokerdemo.core.model.cards.TerrainCard

/**
 * Repräsentiert einen Spieler mit Königreich und Ressourcen.
 */

class Player(val id: String, val name: String, var color: Int) {

    var score: Int = 0
    var currentCard: Card? = null
    var remainingSettlements: Int = 40
        private set




    companion object LocalPlayer{
        lateinit var localPlayer: Player
    }

    //make a contructor that takes a playerdao
    constructor(playerDAO: PlayerDAO) : this(
        playerDAO.id,
        playerDAO.name,
        playerDAO.color,
    ) {
        this.remainingSettlements = playerDAO.remainingSettlements
        this.score = playerDAO.score
    }
}