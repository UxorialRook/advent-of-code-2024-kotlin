/**
 * Simple Point class with some override operators to facilitate the mathematics operations
 */
data class Point(val x: Int, val y: Int) {
    operator fun minus(other: Point) = Point(this.x - other.x, this.y - other.y)
    operator fun plus(other: Point) = Point(this.x + other.x, this.y + other.y)
    operator fun times(scalar: Int) = Point(this.x * scalar, this.y * scalar)
}

/**
 * Return a map with each character associated to a MutableList of points
 *
 * Ex: a -> [Point(0,0), Point(2,2)]
 */
fun createMapOfPoints(lines: List<String>) : MutableMap<Char, MutableList<Point>>
{
    val map = mutableMapOf<Char, MutableList<Point>>()

    // Build the map of character -> list of Points
    lines.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, col ->
            if (col != '.') {
                map.computeIfAbsent(col) { mutableListOf() }.add(Point(rowIndex, colIndex))
            }
        }
    }

    return map
}

fun solvePart1(matrice: List<String>): Int {

    val antennas = createMapOfPoints(matrice)
    val antinodes = mutableSetOf<Point>()

    antennas.forEach { (_, points) ->
        points.forEachIndexed { index, point ->
            for (j in index + 1 until points.size) {
                val pointB = points[j]

                // Calculate the diff
                val diff = pointB - point

                // From the diff we found the antinodes
                antinodes.add(point - diff)
                antinodes.add(pointB + diff)
            }
        }
    }

    // Count valid antinodes within matrice bounds
    return antinodes.count { p ->
        p.x in 0 until matrice.first().length && p.y in matrice.indices
    }
}

fun solvePart2(matrice: List<String>, min: Int, max: Int): Int {
    val antinodes = mutableSetOf<Point>()
    val map = createMapOfPoints(matrice)

    // We keep only the group (2 or more antennas) with the same frequencies
    map.filter { (_ , points) ->
        points.size >= 2
    }

    // Iterate over each group of points
    map.forEach { (_, points) ->
        points.forEachIndexed { index, point ->
            for (j in index + 1 until points.size) {
                val pointB = points[j]

                // We add the antennas themselves
                antinodes.add(point)
                antinodes.add(pointB)

                // same column
                if (point.x == pointB.x) {
                    val yRange = minOf(point.y, pointB.y)..maxOf(point.y, pointB.y)
                    for (y in yRange) {
                        antinodes.add(Point(point.x, y))
                    }
                }

                // Same row
                if (point.y == pointB.y) {
                    val xRange = minOf(point.x, pointB.x)..maxOf(point.x, pointB.x)
                    for (x in xRange) {
                        antinodes.add(Point(x, point.y))
                    }
                }

                // Calculate the diff
                val diff = pointB - point

                for (m in min..max) {
                    antinodes.add(point - diff * m)
                    antinodes.add(pointB + diff * m)
                }
            }
        }
    }

    // Count valid antinodes within matrice bounds
    return antinodes.count { p ->
        p.x in 0 until matrice.first().length && p.y in matrice.indices
    }
}

// Example Usage
fun main() {

    fun part1(input: List<String>): Int {
        return solvePart1(input)
    }

    fun part2(input: List<String>): Int {
        return solvePart2(input, 0, 1000)
    }

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

