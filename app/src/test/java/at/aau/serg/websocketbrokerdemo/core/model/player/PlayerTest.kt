package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerTest {
    private lateinit var player: Player
    private lateinit var field: TerrainField

    @BeforeEach
    fun setUp(){
        field = mock()
        player = Player("1", "Test Player", 1)
    }

    @Test
    fun remainingSettlementsAssertion(){
        assertEquals(40, player.remainingSettlements)
    }

    @Test
    fun buildSettlementTest(){
        assertFailsWith <NotImplementedError> {
            player.buildSettlement(field)
        }
    }
}