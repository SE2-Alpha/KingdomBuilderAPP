package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import kotlin.test.assertFailsWith


class TurnManagerTest {
    private lateinit var players: List<Player>
    private lateinit var turnManagerTest: TurnManager

    @BeforeEach
    fun setUp() {
        players = mock()
        turnManagerTest = TurnManager(players)
    }

    @Test
    fun currentRoundTest(){
        assertEquals(0, turnManagerTest.currentRound)
    }

    @Test
    fun endTurnTest(){
        assertFailsWith<NotImplementedError> {
            turnManagerTest.endTurn()
        }
    }
}