package at.aau.serg.websocketbrokerdemo.core.model.cards

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class KingdomBuilderCardTest {
    private lateinit var testCard: KingdomBuilderCard

    @Test
    fun constructorTest() {
        testCard = KingdomBuilderCard("1", "Test Card", 5)
        assertEquals("1", testCard.id)
        assertEquals("Test Card", testCard.name)
        assertEquals(5, testCard.victoryPoints)
    }

}