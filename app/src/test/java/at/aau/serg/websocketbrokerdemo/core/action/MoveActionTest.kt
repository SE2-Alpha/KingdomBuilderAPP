package at.aau.serg.websocketbrokerdemo.core.action

import at.aau.serg.websocketbrokerdemo.core.actions.MoveAction
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.player.Player

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*

class MoveActionTest {

    private lateinit var player: Player
    private lateinit var fromField: TerrainField
    private lateinit var toField: TerrainField
    private lateinit var moveActionTest: MoveAction

    @BeforeEach
    fun setUp(){
        player = mock(Player::class.java)
        fromField = mock(TerrainField::class.java)
        toField = mock(TerrainField::class.java)
        moveActionTest = MoveAction(player, fromField, toField)
    }

    @Test
    fun executeTest(){
        assertTrue(moveActionTest.execute())
    }

    @Test
    fun undoTest(){
        assertTrue(moveActionTest.undo())
    }

    @Test
    fun isValidMoveTest(){
        assertTrue(moveActionTest.isValidMove())
    }

}