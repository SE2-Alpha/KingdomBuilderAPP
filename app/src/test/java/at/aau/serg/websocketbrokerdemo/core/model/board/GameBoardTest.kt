package at.aau.serg.websocketbrokerdemo.core.model.board

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.*
import kotlin.test.assertFailsWith


class GameBoardTest {
    private lateinit var gameBoardTest: GameBoard
    private lateinit var gameBoardTest2: GameBoard
    private lateinit var field1: TerrainField
    private lateinit var field2: TerrainField

    @BeforeEach
    fun setUp(){
        gameBoardTest2 = mock()
        gameBoardTest = GameBoard(10)
        field1 = mock()
        field2 = mock()
    }


    @ParameterizedTest
    @EnumSource(TerrainType::class)
    fun getFieldsByTypeTest(type: TerrainType){
        assertFailsWith<NotImplementedError> {
            gameBoardTest.getFieldsByType(type)
        }
    }


    @Test
    fun areFieldsAdjacentTest(){
        assertFailsWith<NotImplementedError> {
            gameBoardTest.areFieldsAdjacent(field1, field2)
        }
    }

}