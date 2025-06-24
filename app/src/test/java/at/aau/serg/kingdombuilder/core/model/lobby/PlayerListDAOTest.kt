package at.aau.serg.kingdombuilder.core.model.lobby

import at.aau.serg.kingdombuilder.core.model.player.PlayerDAO
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerListDAOTest {
    private lateinit var playerListDAO: PlayerListDAO
    private lateinit var playerDAO1: PlayerDAO
    private lateinit var playerDAO2: PlayerDAO
    private lateinit var playerDAO3: PlayerDAO

    @Test
    fun testPlayerListDAO(){
        playerDAO1 = PlayerDAO("1", "Player1", 1, 5, 0)
        playerDAO2 = PlayerDAO("2", "Player2", 2, 5, 0)
        playerDAO3 = PlayerDAO("3", "Player3", 3, 5, 0)

        playerListDAO = PlayerListDAO("1234567", listOf(playerDAO1, playerDAO2, playerDAO3))

        assertEquals("1234567", playerListDAO.roomId)
        assertEquals(3, playerListDAO.players.size)
    }

}