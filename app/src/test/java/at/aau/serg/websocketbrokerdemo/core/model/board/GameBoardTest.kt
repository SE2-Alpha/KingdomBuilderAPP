package at.aau.serg.websocketbrokerdemo.core.model.board

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.assertFalse


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


    @Test
    fun areFieldsAdjacentTrueTest(){
        field1 = TerrainField(TerrainType.MOUNTAIN,21)
        field2 = TerrainField(TerrainType.FOREST,42)
        field3 = TerrainField(TerrainType.FLOWERS,41)
        field4 = TerrainField(TerrainType.FLOWERS,61)

        assertTrue(gameBoardTest.areFieldsAdjacent(field1,field2))
        assertTrue(gameBoardTest.areFieldsAdjacent(field3, field4))
    }
    @Test
    fun areFieldsAdjacentFalseTest(){
        field1 = TerrainField(TerrainType.MOUNTAIN,21)
        field2 = TerrainField(TerrainType.FLOWERS,40)
        field3 = TerrainField(TerrainType.FLOWERS,41)
        field4 = TerrainField(TerrainType.FLOWERS,62)

        assertFalse(gameBoardTest.areFieldsAdjacent(field1,field2))
        assertFalse(gameBoardTest.areFieldsAdjacent(field3, field4))
    }

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