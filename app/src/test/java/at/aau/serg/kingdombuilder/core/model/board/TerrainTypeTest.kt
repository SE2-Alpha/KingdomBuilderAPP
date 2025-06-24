package at.aau.serg.kingdombuilder.core.model.board

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class TerrainTypeTest {

    @ParameterizedTest
    @EnumSource(value = TerrainType::class, names = ["GRASS", "CANYON", "DESERT", "FLOWERS", "FOREST", "MOUNTAIN", "SPECIALABILITY", "CITY"])
    fun allowsMovementTrueTest(type:TerrainType){
        assertFalse(type.allowsMovement())
    }

    @Test
    fun allowsMovementTrueTest(){
        assertTrue(TerrainType.WATER.allowsMovement())
    }
}