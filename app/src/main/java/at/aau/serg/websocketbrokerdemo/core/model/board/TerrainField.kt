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

    fun getNeighbours(id: Int): Array<Int>{
        when(id){
            (40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340, 360) -> 5
            (39, 59, 79, 99, 119, 139, 159, 179, 199, 219, 23, 259, 279, 299, 319, 339, 359, 379) -> 5
            id > 20, id < 380 -> 6
            in 1..18 -> arrayOf(id - 1, id + 1, id + 19, id + 20)
            in 381..398 -> arrayOf(id - 1, id + 1, id - 19, id - 20)
            0 -> return arrayOf(1, 21)
            19 -> return arrayOf(18, 38, 39)
            380 -> return arrayOf(360, 361, 381)
            399 -> return arrayOf(379, 398)

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