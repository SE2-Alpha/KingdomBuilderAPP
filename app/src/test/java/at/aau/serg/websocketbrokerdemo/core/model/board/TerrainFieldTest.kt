package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TerrainFieldTest {
    private lateinit var terrainField: TerrainField
    private lateinit var player: Player


    @Test
    fun playerNullTest(){
        terrainField = TerrainField(TerrainType.GRASS,5)
        assertNull(terrainField.builtBy)
    }

//    @ParameterizedTest
//    @EnumSource(TerrainType::class)
//    fun isBuildableTrueTest(type: TerrainType){
//        terrainField = TerrainField(type,5)
//        assertTrue(terrainField.isBuildable)
//    }


}