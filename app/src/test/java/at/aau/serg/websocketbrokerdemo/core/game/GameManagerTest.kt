package at.aau.serg.websocketbrokerdemo.core.game

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import kotlin.test.assertFailsWith

class GameManagerTest {

    private lateinit var players: List<Player>
    private lateinit var turnManager: TurnManager
    private lateinit var gameManagerTest: GameManager

    @BeforeEach
    fun setUp() {
        players = mock()
        turnManager = mock()
        gameManagerTest = GameManager(players, turnManager)
    }

    @Test
    fun initializeGameTest() {
        assertFailsWith<NotImplementedError> {
            gameManagerTest.initializeGame()
        }

    }

    @Test
    fun getCurrentGameStateTest() {
        assertFailsWith<NotImplementedError> {
            gameManagerTest.getCurrentGameState()
        }
    }
}