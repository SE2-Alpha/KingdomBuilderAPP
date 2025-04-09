package at.aau.serg.websocketbrokerdemo.network

import MyStomp
import at.aau.serg.websocketbrokerdemo.core.actions.Action
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertFailsWith

class GameSessionManagerTest {
    private lateinit var testManager: GameSessionManager
    private lateinit var stompClient: MyStomp
    private lateinit var testAction: Action

    @BeforeEach
    fun setUp(){
        testAction = mock()
        stompClient = mock()
        testManager = GameSessionManager(stompClient)
    }

    @Test
    fun sendGameAction(){
        assertFailsWith <NotImplementedError> {
            testManager.sendGameAction(testAction)
        }
    }

}