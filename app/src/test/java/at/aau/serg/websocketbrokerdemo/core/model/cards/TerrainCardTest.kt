package at.aau.serg.websocketbrokerdemo.core.model.cards

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TerrainCardTest {

    private lateinit var testCard: TerrainCard
    private lateinit var testTerrainType: TerrainType

    @Test
    fun constructorTest(){
        testTerrainType = mock()
        testCard = TerrainCard("1","Test Card", TerrainType.GRASS)
        assertEquals("1", testCard.id)
        assertEquals("Test Card", testCard.name)
    }

    @Test
    fun testValidTerrainCardCreation() {
        val card = TerrainCard("t1", "Forest", TerrainType.FOREST)
        assertEquals(TerrainType.FOREST, card.terrainType)
    }

    @Test
    fun testInvalidTerrainCardCreation() {
        assertFailsWith<AssertionError> {
            TerrainCard("t2","Water", TerrainType.WATER)
        }
    }
}