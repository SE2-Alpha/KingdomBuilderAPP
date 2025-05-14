package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.cards.Card
import at.aau.serg.websocketbrokerdemo.core.model.cards.LocationTile
import at.aau.serg.websocketbrokerdemo.core.model.cards.TerrainCard

/**
 * Repräsentiert einen Spieler mit Königreich und Ressourcen.
 */

class Player(val id: String, val name: String, var color: Int, private val gameBoard: GameBoard) {
    /**
     * Verbleibende Siedlungen, die platziert werden können
     */
    var remainingSettlements: Int = 40
        private set
    val kingdom = Kingdom();
    private val handCards = mutableListOf<TerrainCard>()

    var score: Int = 0

    /**
     * @return True, wenn Bauaktion erfolgreich war
     * @param field Zielort für die Siedlung
     */
    fun buildSettlement(field: TerrainField): Boolean {
        if (!canBuildSettlement(field)) return false

        remainingSettlements--
        kingdom.addSettlement(field)
        field.builtBy = this
        return true
    }
    private fun canBuildSettlement(field: TerrainField): Boolean {
        return remainingSettlements > 0 &&
                field.isBuildable &&
                (kingdom.getSettlementCount() == 0 || gameBoard.areFieldAdjacentToKingdom(field,kingdom))
    }

    private fun isFirstSettlement() = kingdom.getSettlementCount() == 0

    fun drawCard(card: TerrainCard){
        handCards.add(card)
    }
    fun playerCard(): TerrainCard? {
        return handCards.removeFirstOrNull()
    }
    fun currentCard(): TerrainCard? {
        return handCards.firstOrNull()
    }
    fun useSpecialAbility(tile: LocationTile): Boolean{
        return tile.specialAction.execute()
    }
    fun validateBuild(field: TerrainField): Boolean{
        return canBuildSettlement(field)
    }
    fun undoBuildSettlement(field: TerrainField): Boolean{
        if (field.builtBy != this) return  false

        field.builtBy = null
        remainingSettlements++
        return true
    }

    companion object LocalPlayer{
        lateinit var localPlayer: Player
    }
}