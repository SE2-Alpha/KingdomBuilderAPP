package at.aau.serg.websocketbrokerdemo.core.action

import at.aau.serg.websocketbrokerdemo.core.actions.BuildAction
import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import kotlin.test.Test
import kotlin.test.assertFailsWith
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
    private lateinit var concreteBuildAction: ConcreteBuildAction

    @BeforeEach
    fun setUp(){
        player = mock()
        terrain = mock()
        gameBoard = mock()
        concreteBuildAction = ConcreteBuildAction(player, terrain, gameBoard)
    }

    @Test
    fun undoTest(){
        assertTrue(concreteBuildAction.undo())
    }

    @Test
    fun executeTest(){
        assertFailsWith<NotImplementedError> {
            concreteBuildAction.execute()
        }
    }
}
