package at.aau.serg.websocketbrokerdemo.core.model.board

import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
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
    @MethodSource("fieldnums")
    fun getNeighboursAmountTest(id: Int){
        terrainField = TerrainField(TerrainType.GRASS, id)
        //if id<0 or id>399, the code should return -1
        if(id < 0 || id > 399){
            assertEquals(1, terrainField.getNeighbours(id).size)
        }else {
            when (id) {
                //only the first and last field should have 2 neighbours
                0, 399 -> assertEquals(2, terrainField.getNeighbours(id).size)

                //upper right and down left have three, as well as half of all the left/right edges
                19, 380, 40, 80, 120, 160, 200, 240, 280, 320, 360, 39, 79, 119, 159, 199, 239, 279, 319, 359 -> assertEquals(
                    3,
                    terrainField.getNeighbours(id).size
                )

                //upper and lower edges have four neighbours (except for the corners)
                in 1..18, in 381..398 -> assertEquals(4, terrainField.getNeighbours(id).size)

                //the other half of the left/right edges has five neighbours
                20, 59, 60, 99, 100, 139, 140, 179, 180, 219, 220, 259, 260, 299, 300, 339, 340, 379 -> assertEquals(
                    5,
                    terrainField.getNeighbours(id).size
                )

                //if not in this category, it should have normal 6 neighbours
                else -> assertEquals(6, terrainField.getNeighbours(id).size)
            }
        }

    }
    companion object {
        @JvmStatic
        fun fieldnums() = (0..400)
    }
}