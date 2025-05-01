package at.aau.serg.websocketbrokerdemo.core.action

import at.aau.serg.websocketbrokerdemo.core.actions.SpecialAction
import at.aau.serg.websocketbrokerdemo.core.model.cards.LocationTile
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertFailsWith

class ConcreteSpecialAction(player: Player,locationTile: LocationTile):SpecialAction(player, locationTile){
    override fun undo(): Boolean {
        return true
    }

}

class SpecialActionTest {

    private lateinit var player: Player
    private lateinit var location: LocationTile
    private lateinit var concreteSpecialAction: ConcreteSpecialAction

    @BeforeEach
    fun setUp(){
        player = mock()
        location = mock()
        concreteSpecialAction = ConcreteSpecialAction(player, location)

    }

    @Test
    fun undoTest(){
        assertTrue(concreteSpecialAction.undo())
    }

    @Test
    fun executeTest(){
        assertFailsWith<NotImplementedError> {
            concreteSpecialAction.execute()
        }
    }
}