package at.aau.serg.websocketbrokerdemo.core.model.cards

import at.aau.serg.websocketbrokerdemo.core.actions.SpecialAction
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

class LocationTileTest {
    private lateinit var testCard: LocationTile
    private lateinit var testSpecialAction: SpecialAction

    @Test
    fun constructorTest() {
        testSpecialAction = mock()
        testCard = LocationTile("1", "Test Card", testSpecialAction)
        assertEquals("1", testCard.id)
        assertEquals("Test Card", testCard.name)
    }
}