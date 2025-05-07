package at.aau.serg.websocketbrokerdemo.core.model.board

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class GameBoardTest {
    private lateinit var gameBoardTest: GameBoard
    private lateinit var field1: TerrainField
    private lateinit var field2: TerrainField
    private lateinit var field3: TerrainField
    private lateinit var field4: TerrainField

    @BeforeEach
    fun setUp(){
        gameBoardTest = GameBoard()
        gameBoardTest.buildGameboard()
    }


    @ParameterizedTest
    @EnumSource(TerrainType::class)
    fun getFieldsByTypeTest(type: TerrainType){
        assertFailsWith<NotImplementedError> {
            gameBoardTest.getFieldsByType(type)
        }
    }

//      I don't know how to do this test
//    @Test
//    fun areFieldsAdjacentTest(){
//        field1 = mock()
//        field2 = mock()
//        assertFailsWith<NotImplementedError> {
//            gameBoardTest.areFieldsAdjacent(field1, field2)
//        }
//    }

//Test Field Placements using getFieldByRowAndCol() - Upper Left, Upper Right, Lower Left, Lower Right
    @Test
    fun upperLeftCornerTest(){
        field1 = gameBoardTest.getFieldByRowAndCol(0,0)//Upper left corner

        assertEquals(0,field1.id)
        assertEquals(TerrainType.FOREST, field1.type)


    }
    @Test
    fun upperRightCornerTest(){
        field2 = gameBoardTest.getFieldByRowAndCol(0,19)//Upper right corner

        assertEquals(19,field2.id)
        assertEquals(TerrainType.GRASS,field2.type)
    }

    @Test
    fun lowerLeftCornerTest(){
        field3 = gameBoardTest.getFieldByRowAndCol(19,0)//Lower left corner

        assertEquals(380,field3.id)
        assertEquals(TerrainType.WATER,field3.type)
    }

    @Test
    fun lowerRightCornerTest(){
        field4 = gameBoardTest.getFieldByRowAndCol(19,19)//Lower right corner

        assertEquals(399,field4.id)
        assertEquals(TerrainType.GRASS,field4.type)
    }
}