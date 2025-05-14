package at.aau.serg.websocketbrokerdemo.core.model.player

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerDAOTest {
    private lateinit var playerDAO: PlayerDAO

    @BeforeEach
    fun setUp(){
        playerDAO = PlayerDAO("1", "Player1", 1, 5, 0)
    }

    @Test
    fun testPlayerDAO() {
        assertEquals("1", playerDAO.id)
        assertEquals("Player1", playerDAO.name)
        assertEquals(1, playerDAO.color)
        assertEquals(5, playerDAO.remainingSettlements)
        assertEquals(0, playerDAO.score)
    }
}