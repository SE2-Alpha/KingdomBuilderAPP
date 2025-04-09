package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

import kotlin.test.assertNull
import kotlin.test.assertTrue

class TerrainFieldTest {
    private lateinit var terrainField: TerrainField
    private lateinit var player: Player
    @BeforeEach
    fun setUp(){
        terrainField = TerrainField(TerrainTypeBuild.GRASS,5,5)
    }
    @Test
    fun playerNullTest(){
        assertNull(terrainField.builtBy)
    }

    @Test
    fun isBuildableTest(){
        assertTrue(terrainField.isBuildable)
    }

}