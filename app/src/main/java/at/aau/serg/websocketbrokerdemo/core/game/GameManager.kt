package at.aau.serg.websocketbrokerdemo.core.game

import android.app.GameState
import android.os.Build
import androidx.annotation.RequiresApi
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.cards.LocationTile
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
    private val location = mutableListOf<LocationTile>()

    /**
     * Initialisiert das Spiel:
     * - Erstellt das Spielbrett
     * - Mischt die Kartenstapel
     * - Setzt Startwerte für Spieler
     */
    fun initializeGame() {
        val availableQuadrants = listOf(1, 2, 3, 4)
        val selectedQuadrants = availableQuadrants.shuffled().take(4)

        gameBoard.buildGameboard(selectedQuadrants)

        //Geländekarten initialisieren (5x jedes Terrain)
        listOf(
            TerrainType.GRASS to "Gras",
            TerrainType.CANYON to "Canyon",
            TerrainType.DESERT to "Wüste",
            TerrainType.FLOWERS to "Blume",
            TerrainType.FOREST to "Wald"
        ).forEach { (type, name) ->
            repeat(5) {
                terrainDeck.add(TerrainCard(type.name, name, type))
            }
        }
        terrainDeck.shuffle()

        //Karten an Spieler verteilen
        players.forEach { player ->
            if (terrainDeck.isNotEmpty()) {
                player.drawCard(terrainDeck.removeAt(0))
            } else {
                throw IllegalStateException("Deck ist leer beim Initialisieren!")
            }
        }
    }

    fun isGameOver(): Boolean{
        return players.any { it.remainingSettlements == 0 }
    }

    /**
     * @return Aktueller Spielzustand (Brett, Spielerstände, Karten)
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getCurrentGameState(): GameState{
        TODO()
    }
}

