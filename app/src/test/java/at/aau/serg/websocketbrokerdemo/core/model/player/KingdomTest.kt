package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KingdomTest {
    private val gameBoard = mock(GameBoard::class.java)
    private val kingdom = Kingdom()

    @Test
    fun testKingdomExpansion(){
        val field1 = TerrainField(TerrainType.GRASS, 1)
        val field2 = TerrainField(TerrainType.GRASS, 2)

        kingdom.addSettlement(field1)
        kingdom.addSettlement(field2)

        assertEquals(2, kingdom.getSettlementCount())
    }

    @Test
    fun testAdjacentFieldDetection() {
        val field = mock(TerrainField::class.java)
        val adjacentFields = listOf(mock(TerrainField::class.java))
        `when`(gameBoard.getAdjacentFields(field)).thenReturn(adjacentFields)

        kingdom.addSettlement(field)
        val result = kingdom.getAdjacentFields(gameBoard)

        assertTrue(result.containsAll(adjacentFields))
    }
}