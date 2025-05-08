package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import kotlin.test.assertFailsWith


class TurnManagerTest {
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var players: List<Player>
    private lateinit var turnManager: TurnManager

    @BeforeEach
    fun setUp() {
        player1 = mock(Player::class.java).apply {
            `when`(id).thenReturn("1")
        }
        player2 = mock(Player::class.java).apply {
            `when`(id).thenReturn("2")
        }
        players = listOf(player1, player2)
        turnManager = TurnManager(players)
    }

    @Test
    fun currentRoundTest(){
        assertEquals(0, turnManager.currentRound)
    }

    @Test
    fun endTurnTest(){
        assertFailsWith<NotImplementedError> {
            turnManager.endTurn()
        }
    }

    @Test
    fun testPlayerRotation(){
        assertEquals("1", turnManager.currentPlayer.id)
        turnManager.endTurn()
        assertEquals("2", turnManager.currentPlayer.id)
        turnManager.endTurn()
        assertEquals(1, turnManager.currentRound)
    }

    @Test
    fun testRoundCounting(){
        repeat(players.size * 3) { turnManager.endTurn() }
        assertEquals(3, turnManager.currentRound)
    }

    @Test
    fun `should increment round after full rotation`() {
        turnManager.endTurn() // Player 2
        turnManager.endTurn() // Player 1, round 1
        assertEquals(1, turnManager.currentRound)
    }
}