package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertFailsWith

class KingdomTest {
    private lateinit var kingdom: Kingdom
    private lateinit var field: TerrainField

    @BeforeEach
    fun setUp(){
        field = mock()
        kingdom = Kingdom()
    }

    @Test
    fun getSettlementCountTest(){
        assertFailsWith <NotImplementedError> {
            kingdom.getSettlementCount()
        }
    }

    @Test
    fun addSettlementTest(){
        assertFailsWith <NotImplementedError> {
            kingdom.addSettlement(field)
        }
    }
}