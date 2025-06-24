package at.aau.serg.kingdombuilder.core.model.board
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import at.aau.serg.kingdombuilder.core.model.player.Player
import com.example.myapplication.R

/**
 * Einzelnes Feld auf dem Spielbrett.
 */

data class TerrainField(var type: TerrainType, val id: Int, var builtBy: Player? = null, var ownerSinceRound: Int = -1) { //needs quadrant number and ids of neighbours
    /**
     * Referenz auf den Spieler, der hier gebaut hat (null wenn frei)
     */

    /**
     * @return True, wenn das Feld aktuell bebaut werden kann
     */
    val isBuildable: Boolean
        get() = type.isBuildable && builtBy == null

    /**
     * @return Gibt alle Nachbarn in einem Array zurück. Falls die Id nicht zwischen 0 und 399 liegt, gibt es -1 zurück
     */
    fun getNeighbours(id: Int): Array<Int>{
        if (id > -1 && id < 400) {

           return when (id) {
                20, 60, 100, 140, 180, 220, 260, 300, 340 -> arrayOf(
                    id - 20,
                    id - 19,
                    id + 1,
                    id + 20,
                    id + 21
                )

                40, 80, 120, 160, 200, 240, 280, 320, 360 -> arrayOf(
                    id - 20,
                    id + 1,
                    id + 20
                )

                39, 79, 119, 159, 199, 239, 279, 319, 359 -> arrayOf(
                    id - 20,
                    id - 1,
                    id + 20
                )

                59, 99, 139, 179, 219, 259, 299, 339, 379 -> arrayOf(
                    id - 21,
                    id - 20,
                    id - 1,
                    id + 19,
                    id + 20
                )

                in 1..18 ->  arrayOf(id - 1, id + 1, id + 19, id + 20)
                in 381..398 ->  arrayOf(id - 1, id + 1, id - 19, id - 20)
                0 ->  arrayOf(1, 21)
                19 ->  arrayOf(18, 38, 39)
                380 ->  arrayOf(360, 361, 381)
                399 ->  arrayOf(379, 398)

                in 21..38, in 61..78, in 101..118, in 141..158, in 181..198, in 221..238, in 261..278, in 301..318, in 341..378 ->  arrayOf(
                    id - 20,
                    id - 19,
                    id - 1,
                    id + 1,
                    id + 20,
                    id + 21
                )

                else -> {
                     arrayOf(id - 21, id - 20, id - 1, id + 1, id + 19, id + 20)
                }
            }
        }
        return arrayOf(-1)
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