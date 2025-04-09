package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*

class GameManagerTest {

    private lateinit var players: List<Player>
    private lateinit var turnManager: TurnManager
    private lateinit var gameManagerTest: GameManager

    @BeforeEach
    fun setUp() {
        players = mock()
        turnManager = mock()
        gameManagerTest = mock()
    }

    @Test
    fun initializeGameTest() {
        gameManagerTest.initializeGame()
        verify(gameManagerTest).initializeGame()

    }

    @Test
    fun getCurrentGameStateTest() {
        gameManagerTest.getCurrentGameState()
        verify(gameManagerTest).getCurrentGameState()
    }
}