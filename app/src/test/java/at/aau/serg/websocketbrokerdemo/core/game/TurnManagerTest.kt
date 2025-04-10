package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*


class TurnManagerTest {
    private lateinit var players: List<Player>
    private lateinit var turnManagerTest: TurnManager
    private lateinit var turnManagerTest2: TurnManager

    @BeforeEach
    fun setUp() {
        players = mock()
        turnManagerTest = mock()
        turnManagerTest2 = TurnManager(players)
    }

    @Test
    fun currentRoundTest(){
        assertEquals(0, turnManagerTest2.currentRound)
    }

    @Test
    fun endTurnTest(){
        turnManagerTest.endTurn()
        verify(turnManagerTest).endTurn()
    }
}