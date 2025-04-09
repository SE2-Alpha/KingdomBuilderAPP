package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class TerrainFieldTest {
    private lateinit var terrainField: TerrainField
    private lateinit var player: Player


    @Test
    fun playerNullTest(){
        terrainField = TerrainField(TerrainTypeBuild.GRASS,5,5)
        assertNull(terrainField.builtBy)
    }

    @ParameterizedTest
    @EnumSource(TerrainTypeBuild::class)
    fun isBuildableTrueTest(type: TerrainType){
        terrainField = TerrainField(type,5,5)
        assertTrue(terrainField.isBuildable)
    }

    @ParameterizedTest
    @EnumSource(TerrainTypeSpecial::class)
    fun isBuildableFalseTest(type: TerrainType){
        terrainField = TerrainField(type,5,5)
        assertFalse(terrainField.isBuildable)
    }

}