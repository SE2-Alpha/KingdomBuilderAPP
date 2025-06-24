package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.cards.Card

/**
 * Repräsentiert einen Spieler mit Königreich und Ressourcen.
 */

class Player(val id: String, val name: String, var color: Int) {

    var score: Int = 0
    var currentCard: Card? = null
    var remainingSettlements: Int = 40
        set(value){
            field = value.coerceAtLeast(0)
        }


    companion object LocalPlayer{
        lateinit var localPlayer: Player
    }

    //make a constructor that takes a playerDao
    constructor(playerDAO: PlayerDAO) : this(
        playerDAO.id,
        playerDAO.name,
        playerDAO.color,
    ) {
        this.remainingSettlements = playerDAO.remainingSettlements
        this.score = playerDAO.score
    }
}