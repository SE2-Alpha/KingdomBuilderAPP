package at.aau.serg.kingdombuilder.core.model.board

import junit.framework.TestCase.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


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

    @Test
    fun testAdjacentFields() {
        val field1 = gameBoardTest.getFieldByRowAndCol(0,0)
        val field2 = gameBoardTest.getFieldByRowAndCol(0,1)
        assertTrue(gameBoardTest.areFieldsAdjacent(field1,field2))
    }

    @Test
    fun testNonAdjacentFields() {
        val field1 = gameBoardTest.getFieldByRowAndCol(0,0)
        val field2 = gameBoardTest.getFieldByRowAndCol(2,2)
        assertFalse(gameBoardTest.areFieldsAdjacent(field1,field2))
    }

    @Test
    fun testGetFieldsByType() {
        val waterFields = gameBoardTest.getFieldsByType(TerrainType.WATER)
        assertTrue(waterFields.size > 50)
    }

    @Test
    fun `adjacent fields cross quadrant boundaries`() {
        val lastFieldQuadrant1 = gameBoardTest.getFieldByRowAndCol(9, 9)

        val firstFieldQuadrant2 = gameBoardTest.getFieldByRowAndCol(10, 10)


        println("Field 1 ID: ${lastFieldQuadrant1.id}, Position: (${lastFieldQuadrant1.id % 20}, ${lastFieldQuadrant1.id / 20})")
        println("Field 2 ID: ${firstFieldQuadrant2.id}, Position: (${firstFieldQuadrant2.id % 20}, ${firstFieldQuadrant2.id / 20})")

        assertTrue(
            gameBoardTest.areFieldsAdjacent(lastFieldQuadrant1, firstFieldQuadrant2),
            "Felder an Quadrant-Grenzen sollten als benachbart gelten"
        )
    }

    @Test
    fun `test specific adjacent positions`() {
        // Horizontale Nachbarn
        val field1 = gameBoardTest.getFieldByRowAndCol(5, 5)
        val field2 = gameBoardTest.getFieldByRowAndCol(5, 6)
        assertTrue(gameBoardTest.areFieldsAdjacent(field1, field2))

        // Vertikale Nachbarn (versetzt)
        val field3 = gameBoardTest.getFieldByRowAndCol(5, 5)
        val field4 = gameBoardTest.getFieldByRowAndCol(6, 5)
        assertTrue(gameBoardTest.areFieldsAdjacent(field3, field4))

        // Diagonale Nachbarn
        val field5 = gameBoardTest.getFieldByRowAndCol(5, 5)
        val field6 = gameBoardTest.getFieldByRowAndCol(6, 6)
        assertTrue(gameBoardTest.areFieldsAdjacent(field5, field6))

        // Nicht Nachbarn
        val field7 = gameBoardTest.getFieldByRowAndCol(0, 0)
        val field8 = gameBoardTest.getFieldByRowAndCol(5, 5)
        assertFalse(gameBoardTest.areFieldsAdjacent(field7, field8))
    }

    @Test
    fun `field types match quadrant definitions`() {
        val field = gameBoardTest.getFieldByRowAndCol(0, 0)
        assertEquals(TerrainType.FOREST, field.type)
    }

    @Test
    fun `special ability fields exist`() {
        val specialFields = gameBoardTest.getFieldsByType(TerrainType.SPECIALABILITY)
        assertTrue(specialFields.size >= 4) // min. 1 pro Quadrant
    }

}