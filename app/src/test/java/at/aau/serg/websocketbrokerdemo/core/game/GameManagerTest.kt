package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import kotlin.test.assertFailsWith

class GameManagerTest {

    private lateinit var players: List<Player>
    private lateinit var turnManager: TurnManager
    private lateinit var gameManagerTest: GameManager
    private lateinit var gameBoard: GameBoard

    @BeforeEach
    fun setup() {
        players = listOf(
            mock(Player::class.java),
            mock(Player::class.java)
        )
        gameBoard = mock(GameBoard::class.java)
        gameManagerTest = GameManager(players, TurnManager(players), gameBoard)
    }

    @Test
    fun `initializeGame creates valid deck`() {
        gameManagerTest.initializeGame()
        assertEquals(25-players.size, gameManagerTest.getTerrainDeckSize())
    }

    @Test
    fun `game over detection works for multiple players`() {
        `when`(players[0].remainingSettlements).thenReturn(0)
        `when`(players[1].remainingSettlements).thenReturn(5)
        assertTrue(gameManagerTest.isGameOver())
    }

    @Test
    fun `drawing from empty deck throws`() {
        gameManagerTest.getTerrainDeckClear()
        assertThrows<IllegalStateException> {
            gameManagerTest.drawCardFromDeck()
        }
    }

    @Test
    fun `player order persistence between rounds`() {
        val turnManager = TurnManager(players)
        val manager = GameManager(players, turnManager, gameBoard)
        repeat(players.size * 2) { turnManager.endTurn() }
        assertEquals(players[0], turnManager.currentPlayer)
    }

}