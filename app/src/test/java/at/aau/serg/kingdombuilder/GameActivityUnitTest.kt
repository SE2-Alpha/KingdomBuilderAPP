package at.aau.serg.kingdombuilder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.cos
import kotlin.math.sin

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GameActivityUnitTest {

        @Test
        fun testGetQuadrant() {
            // Teste Quartile basierend auf Zeilen und Spalten des 20x20-Gitters

            // Quadrant 1: obere linke Hälfte
            assertEquals("Quadrant 1", getQuadrant(row = 0, col = 0))
            assertEquals("Quadrant 1", getQuadrant(row = 5, col = 5))

            // Quadrant 2: obere rechte Hälfte
            assertEquals("Quadrant 2", getQuadrant(row = 0, col = 10))
            assertEquals("Quadrant 2", getQuadrant(row = 5, col = 15))

            // Quadrant 3: untere linke Hälfte
            assertEquals("Quadrant 3", getQuadrant(row = 10, col = 0))
            assertEquals("Quadrant 3", getQuadrant(row = 15, col = 5))

            // Quadrant 4: untere rechte Hälfte
            assertEquals("Quadrant 4", getQuadrant(row = 10, col = 10))
            assertEquals("Quadrant 4", getQuadrant(row = 15, col = 15))
        }

        @Test
        fun testPointInHexagon_inside() {
            // Definiere einen Hexagon-Mittelpunkt und die Seitenlänge
            val centerX = 100f
            val centerY = 100f
            val side = 30f

            // Der Mittelpunkt muss innerhalb des Hexagons liegen
            assertTrue(pointInHexagon(centerX, centerY, centerX, centerY, side))

            // Überprüfe einen der Eckpunkte des Hexagons (z. B. am Winkel 90°)
            val vertexX = centerX + side * cos(Math.toRadians(90.0)).toFloat()
            val vertexY = centerY + side * sin(Math.toRadians(90.0)).toFloat()
            // Der Eckpunkt sollte (auf dem Rand) als innerhalb erkannt werden
            assertTrue(pointInHexagon(vertexX, vertexY, centerX, centerY, side))

            // Teste einen Punkt, der nahe am Rand liegt
            val pointNearEdgeX = centerX + side * 0.4f
            val pointNearEdgeY = centerY + side * 0.65f
            assertTrue(pointInHexagon(pointNearEdgeX, pointNearEdgeY, centerX, centerY, side))
        }

        @Test
        fun testPointInHexagon_outside() {
            val centerX = 100f
            val centerY = 100f
            val side = 30f

            // Ein Punkt deutlich rechts außerhalb des Hexagons
            val outsideX = centerX + side  // Dieser Punkt liegt weiter rechts als zulässig
            val outsideY = centerY
            assertFalse(pointInHexagon(outsideX, outsideY, centerX, centerY, side))

            // Ein Punkt weit oberhalb des Hexagons
            val outsideX2 = centerX
            val outsideY2 = centerY - side * 2
            assertFalse(pointInHexagon(outsideX2, outsideY2, centerX, centerY, side))
        }
    }