package at.aau.serg.websocketbrokerdemo.network

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameStateSerializerTest {
    private lateinit var gameStateSerializer: GameStateSerializer

    /*
    @BeforeEach
    fun setUp(){
        gameStateSerializer = GameStateSerializer()
    }
    */

    @Test
    fun classCallTest(){
        gameStateSerializer = GameStateSerializer()
    }
}