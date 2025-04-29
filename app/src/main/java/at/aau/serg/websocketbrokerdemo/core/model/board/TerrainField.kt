package at.aau.serg.websocketbrokerdemo.core.model.board
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import at.aau.serg.websocketbrokerdemo.core.model.player.Player
import com.example.myapplication.R

/**
 * Einzelnes Feld auf dem Spielbrett.
 */

class TerrainField(val type: TerrainType, val id: Int) { //needs quadrant number and ids of neighbours
    /**
     * Referenz auf den Spieler, der hier gebaut hat (null wenn frei)
     */

    var builtBy: Player? = null


    /**
     * @return True, wenn das Feld aktuell bebaut werden kann
     */
    val isBuildable: Boolean
        get() = type.isBuildable && builtBy == null

    fun getNeighbours(id: Int): Int{
        return when(id){
            (40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340, 360) -> 5
            (39, 59, 79, 99, 119, 139, 159, 179, 199, 219, 23, 259, 279, 299, 319, 339, 359, 379) -> 5
            (id > 20 && id < 380) -> 6
            id > 0 && id < 19 -> 4
            id > 380 && id < 400 -> 4
            0 -> 2
            20 -> 3
            380 -> 3
            400 -> 2

            else -> {}
        } as Int
    }

    fun getColor(context: Context): Color {
            return Color(when (type) {
                TerrainType.GRASS -> ContextCompat.getColor(context, R.color.terrain_grass)
                TerrainType.CANYON -> ContextCompat.getColor(context, R.color.terrain_canyon)
                TerrainType.DESERT -> ContextCompat.getColor(context, R.color.terrain_desert)
                TerrainType.FLOWERS -> ContextCompat.getColor(context, R.color.terrain_flowers)
                TerrainType.WATER -> ContextCompat.getColor(context, R.color.terrain_water)
                TerrainType.MOUNTAIN -> ContextCompat.getColor(context, R.color.terrain_mountain)
                TerrainType.FOREST -> ContextCompat.getColor(context, R.color.terrain_forest)
                TerrainType.SPECIALABILITY -> ContextCompat.getColor(context, R.color.terrain_specialability)
                TerrainType.CITY -> ContextCompat.getColor(context, R.color.terrain_city)
            })
        }

}