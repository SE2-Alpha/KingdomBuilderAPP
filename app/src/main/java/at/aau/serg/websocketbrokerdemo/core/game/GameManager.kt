package at.aau.serg.websocketbrokerdemo.core.game

import android.app.GameState
import android.os.Build
import androidx.annotation.RequiresApi
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.cards.TerrainCard
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

/**
 * Zentrale Steuernungsklasse für den Spielablauf.
 * Verwaltet Spielinitialisierung, Rundenlogik und den globalen Spielzustand.
 */

class GameManager(private val players: List<Player>, private  val turnManager: TurnManager, private val gameBoard: GameBoard) {
    /**
     * Aktives Spielbrett mit Terrainfeldern
     */
    private val terrainDeck = mutableListOf<TerrainCard>()


    fun isGameOver(): Boolean{
        return players.any { it.remainingSettlements == 0 }
    }

    fun getTerrainDeckSize(): Int {
        return terrainDeck.size
    }

    fun getTerrainDeckClear() {
        terrainDeck.clear()
    }

    fun drawCardFromDeck(): TerrainCard {
        if (terrainDeck.isNotEmpty()) {
            return terrainDeck.removeAt(0)
        } else {
            throw IllegalStateException("Deck ist leer!")
        }
    }

}

