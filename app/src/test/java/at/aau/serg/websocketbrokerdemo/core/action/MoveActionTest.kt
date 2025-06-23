package at.aau.serg.websocketbrokerdemo.core.action

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.player.Kingdom
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.*

class MoveActionTest {

    private lateinit var player: Player
    private lateinit var fromField: TerrainField
    private lateinit var toField: TerrainField
    private lateinit var gameBoard: GameBoard
    private lateinit var moveAction: MoveAction

    @BeforeEach
    fun setUp() {
        player = mock(Player::class.java).apply {
            `when`(id).thenReturn("player1")
            `when`(kingdom).thenReturn(Kingdom())  // Wichtig: Kingdom mocken
        }
        gameBoard = mock(GameBoard::class.java)
        fromField = TerrainField(TerrainType.GRASS, 1).apply {
            builtBy = player
        }
        toField = TerrainField(TerrainType.GRASS, 2)
        moveAction = MoveAction(player, fromField, toField, gameBoard)
    }

    @Test
    fun `execute succeeds when move is valid`() {
        `when`(gameBoard.areFieldsAdjacent(fromField, toField)).thenReturn(true)
        assertTrue(moveAction.execute())
        assertNull(fromField.builtBy)
        assertEquals(player, toField.builtBy)
    }

    @Test
    fun `execute fails when fields are not adjacent`() {
        `when`(gameBoard.areFieldsAdjacent(fromField, toField)).thenReturn(false)
        assertFalse(moveAction.execute())
    }

    @Test
    fun `execute fails when source not owned by player`() {
        fromField.builtBy = null
        assertFalse(moveAction.execute())
    }

    @Test
    fun `execute fails when target not buildable`() {
        toField.type = TerrainType.WATER // Nicht bebaubar
        `when`(gameBoard.areFieldsAdjacent(fromField, toField)).thenReturn(true)
        assertFalse(moveAction.execute())
    }

    @Test
    fun `undo restores original state`() {
        `when`(gameBoard.areFieldsAdjacent(fromField, toField)).thenReturn(true)
        moveAction.execute()
        assertTrue(moveAction.undo())
        assertEquals(player, fromField.builtBy)
        assertNull(toField.builtBy)
    }

    @Test
    fun `isValidMove checks all conditions`() {
        `when`(gameBoard.areFieldsAdjacent(fromField, toField)).thenReturn(true)
        assertTrue(moveAction.isValidMove())
    }
}