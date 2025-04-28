package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.mock

class TerrainFieldTest {
    private lateinit var terrainField: TerrainField
    private lateinit var player: Player


    @Test
    fun playerNullTest(){
        terrainField = TerrainField(TerrainType.GRASS,5)
        assertNull(terrainField.builtBy)
    }

    @ParameterizedTest
    @EnumSource(value = TerrainType::class, names = ["GRASS", "CANYON", "DESERT", "FLOWERS", "FOREST",])
    fun isBuildableTrueTest(type: TerrainType){
        terrainField = TerrainField(type,22)
        assertNull(terrainField.builtBy)
        assertTrue(terrainField.isBuildable)
    }

    @ParameterizedTest
    @EnumSource(value = TerrainType::class, names =["WATER", "MOUNTAIN", "SPECIALABILITY", "CITY"])
    fun isBuildableFalseTest(type: TerrainType){
        terrainField = TerrainField(type,5)
        assertFalse(terrainField.isBuildable)
    }

    @Test
    fun isBuildableFalseOccupiedTest(){
        player = mock()
        terrainField = TerrainField(TerrainType.GRASS,5)
        terrainField.builtBy = player
        assertFalse(terrainField.isBuildable)
    }


}