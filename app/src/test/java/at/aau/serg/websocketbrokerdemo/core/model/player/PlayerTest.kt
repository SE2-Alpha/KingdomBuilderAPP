package at.aau.serg.websocketbrokerdemo.core.model.player

import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.vector.Group
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PlayerTest {
    private lateinit var player: Player
    private lateinit var gameBoard: GameBoard
    private lateinit var validField: TerrainField
    private lateinit var invalidField: TerrainField


    @BeforeEach
    fun setUp(){
        gameBoard = GameBoard()
        gameBoard.buildGameboard()
        player = Player("1", "Player1", 0, gameBoard)
        validField = gameBoard.getFieldByRowAndCol(0,0).apply { type = TerrainType.GRASS }
        invalidField = TerrainField(TerrainType.WATER, 999)
    }

    @Test
    fun remainingSettlementsAssertion(){
        assertEquals(40, player.remainingSettlements)
    }

    @Test
    fun `buildSettlement reduces remaining settlements`() {
        assertTrue(player.buildSettlement(validField))
        assertEquals(39, player.remainingSettlements)
    }

    @Test
    fun `buildSettlement reduces settlements and updates kingdom`() {
        assertTrue(player.buildSettlement(validField))
        assertEquals(39, player.remainingSettlements)
        assertTrue(player.kingdom.getSettlementCount() == 1)
    }

    @Test
    fun `cannot build on non-buildable field`() {
        val waterField = TerrainField(TerrainType.WATER, 999)
        assertFalse(player.buildSettlement(waterField))
    }

    @Test
    fun `undo build restores state`() {
        player.buildSettlement(validField)
        assertTrue(player.undoBuildSettlement(validField))
        assertEquals(40, player.remainingSettlements)
        assertNull(validField.builtBy)
    }

    @Test
    fun `card management handles empty hand correctly`() {
        assertNull(player.playerCard())
        assertNull(player.currentCard())
    }

    @Test
    fun `validateBuild checks first settlement rules`() {
        val isolatedField = gameBoard.getFieldByRowAndCol(15,15)
        assertTrue(player.validateBuild(isolatedField))
    }

    @Test
    fun `validateBuild enforces kingdom connection`() {
        player.buildSettlement(validField)
        val disconnectedField = gameBoard.getFieldByRowAndCol(5,5)
        assertFalse(player.validateBuild(disconnectedField))
    }
}