import kotlin.math.abs

fun main() {

    fun isValidMatriceValue(matrice: List<String>, position: Pair<Int, Int>): Boolean {
        val (row, col) = position
        val charToFind = listOf('M', 'S')
        return row in matrice.indices && col in matrice[row].indices && matrice[row][col] in charToFind
    }

    fun hasAnXMASPattern(matrice: List<String>, position : Pair<Int, Int> ) : Boolean
    {
        // 1. We store all the directions possible
        val downRight = Pair(1, 1)
        val downLeft = Pair(1, -1)
        val upLeft = Pair(-1, -1)
        val upRight = Pair(-1, 1)

        // 2. We calculate the relative position
        val upRightPos = Pair(position.first + upRight.first, position.second + upRight.second)
        val downLeftPos = Pair(position.first + downLeft.first, position.second + downLeft.second)
        val upLeftPos = Pair(position.first + upLeft.first, position.second + upLeft.second)
        val downRightPos = Pair(position.first + downRight.first, position.second + downRight.second)

        val isUpRightValid = isValidMatriceValue(matrice, upRightPos)
        val isDownLeftValid = isValidMatriceValue(matrice, downLeftPos)
        val isDownRightValid = isValidMatriceValue(matrice, downRightPos)
        val isUpLeftValid = isValidMatriceValue(matrice, upLeftPos)

        if(!(isUpRightValid && isDownLeftValid && isDownRightValid && isUpLeftValid)) {
            return false;
        }

        val upRightVal = matrice[upRightPos.first][upRightPos.second]
        val downLeftVal = matrice[downLeftPos.first][downLeftPos.second]
        val downRightVal = matrice[downRightPos.first][downRightPos.second]
        val upLeftVal = matrice[upLeftPos.first][upLeftPos.second]

        return (upRightVal != downLeftVal) && (upLeftVal != downRightVal)
    }

    fun findXMAS(matrice: List<String>): Int {
        var number = 0

        for (row in matrice.indices) {
            for (col in matrice[row].indices) {
                if (matrice[row][col] == 'A') {
                    if(hasAnXMASPattern(matrice, Pair(row, col))) {
                        number += 1
                    }
                }
            }
        }

        return number
    }

    fun findTheWord(matrice : List<String>, word : String) : List<List<Pair<Int, Int>>>
    {
        // 1. We store all the directions possible
        val directions = listOf(
            Pair(0, 1),   // Right
            Pair(1, 0),   // Down
            Pair(1, 1),   // Down-right
            Pair(1, -1),  // Down-left
            Pair(0, -1),  // Left
            Pair(-1, 0),  // Up
            Pair(-1, -1), // Up-left
            Pair(-1, 1)   // Up-right
        )

        // 2. Some global variables to store the limits and the final result
        val numberRows = matrice.size;
        val numberCols = matrice[0].length
        val result = mutableListOf<List<Pair<Int, Int>>>()

        // 3. Foreach column and each row and foreach position we want to test if we found the new word
        for (row in 0 until numberRows) {
            for (column in 0 until numberCols) {
                for (dir in directions) {
                    val positions = mutableListOf<Pair<Int, Int>>()
                    var found = true

                    for (indice in word.indices) {
                        val newRow = row + indice * dir.first
                        val newCol = column + indice * dir.second

                        if (newRow !in 0 until numberRows || newCol !in 0 until numberCols || matrice[newRow][newCol] != word[indice]) {
                            found = false
                            break
                        }
                        positions.add(Pair(newRow, newCol))
                    }

                    if (found) {
                        result.add(positions)
                    }
                }
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return findTheWord(input, "XMAS").size
    }

    fun part2(input: List<String>): Int {
        return findXMAS(input);
    }

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
