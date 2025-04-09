package at.aau.serg.websocketbrokerdemo.core.model.cards

import at.aau.serg.websocketbrokerdemo.core.model.board.TerrainTypeBuild
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

class TerrainCardTest {

    private lateinit var testCard: TerrainCard
    private lateinit var testTerrainType: TerrainTypeBuild

    @Test
    fun constructorTest(){
        testTerrainType = mock()
        testCard = TerrainCard("1","Test Card", testTerrainType)
        assertEquals("1", testCard.id)
        assertEquals("Test Card", testCard.name)
    }
}