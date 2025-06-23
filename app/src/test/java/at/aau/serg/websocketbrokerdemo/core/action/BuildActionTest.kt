package at.aau.serg.websocketbrokerdemo.core.action

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConcreteBuildAction(player: Player, terrain: TerrainField, gameBoard: GameBoard): BuildAction(player, terrain, gameBoard){
    override fun undo(): Boolean {
        return true
    }
}

class BuildActionTest {

    private lateinit var player: Player
    private lateinit var terrain: TerrainField
    private lateinit var gameBoard: GameBoard
    private lateinit var invalidField: TerrainField
    private lateinit var validField: TerrainField
    private lateinit var concreteBuildAction: ConcreteBuildAction

    @BeforeEach
    fun setUp(){
        player = mock(Player::class.java).apply{
            `when`(remainingSettlements).thenReturn(5)
        }
        terrain = mock()
        validField = TerrainField(TerrainType.GRASS, 1).apply {
            builtBy = null
        }
        invalidField = TerrainField(TerrainType.WATER, 2)
        gameBoard = mock(GameBoard::class.java)
        concreteBuildAction = ConcreteBuildAction(player, terrain, gameBoard)
    }

    @Test
    fun undoTest(){
        assertTrue(concreteBuildAction.undo())
    }

    @Test
    fun `execute fails when no settlements left`() {
        `when`(player.remainingSettlements).thenReturn(0)
        val action = BuildAction(player, validField, gameBoard)
        assertFalse(action.execute())
    }

    @Test
    fun `execute fails when field not buildable`() {
        val action = BuildAction(player, invalidField, gameBoard)
        assertFalse(action.execute())
    }

    @Test
    fun `undo restores settlement count`() {
        `when`(player.validateBuild(validField)).thenReturn(true)
        `when`(player.buildSettlement(validField)).thenAnswer {
            validField.builtBy = player

            `when`(player.remainingSettlements).thenReturn(4)
            true
        }

        `when`(player.undoBuildSettlement(validField)).thenAnswer {
            validField.builtBy = null
            `when`(player.remainingSettlements).thenReturn(5)
            true
        }

        val action = BuildAction(player, validField, gameBoard)
        val executed = action.execute()
        val undone = action.undo()

        assertTrue(executed)
        assertTrue(undone)
        verify(player).undoBuildSettlement(validField)
    }


    @Test
    fun `cannot undo non-executed action`() {
        val action = BuildAction(player,validField,gameBoard)
        assertFalse(action.undo())
    }
}
