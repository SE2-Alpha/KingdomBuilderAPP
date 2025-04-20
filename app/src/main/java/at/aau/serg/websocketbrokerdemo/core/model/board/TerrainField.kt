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