package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
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

    @ParameterizedTest
    @CsvSource("0", "5", "7", "15", "19", "20", "25", "39", "40", "45", "50", "55", "60", "65", "70", "75", "79", "85", "90", "99",
        "111", "129", "136", "148", "150", "166", "177", "189", "199", "200", "222", "229", "233", "244", "251", "300", "380", "381", "399" ,"455")
    fun getNeighboursAmountTest(id: Int){
        terrainField = TerrainField(TerrainType.GRASS, id)
        terrainField.getNeighbours(id)
        assertEquals(6,terrainField.getNeighbours(id).size)


    }

}