package at.aau.serg.websocketbrokerdemo.core.model.player

import at.aau.serg.websocketbrokerdemo.core.model.board.GameBoard
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainField
import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerTest {
    private lateinit var player: Player
    private lateinit var gameBoard: GameBoard
    private lateinit var validField: TerrainField
    private lateinit var invalidField: TerrainField


    @BeforeEach
    fun setUp(){
        gameBoard = GameBoard()
        gameBoard.buildGameboard()
        player = Player("1", "Player1", 0)
        validField = gameBoard.getFieldByRowAndCol(0,0).apply { type = TerrainType.GRASS }
        invalidField = TerrainField(TerrainType.WATER, 999)
    }

    @Test
    fun remainingSettlementsAssertion(){
        assertEquals(40, player.remainingSettlements)
    }
}