import kotlin.math.abs

fun main() {

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

        var upRightToBottomLeftIsOk = false
        var upLeftTobottomRightIsOk = false

        // 3. Starting with the upRight -> bottomLeft
        val isUpRightValid = upRightPos.first in matrice.indices &&
                upRightPos.second in matrice[upRightPos.first].indices &&
                (matrice[upRightPos.first][upRightPos.second] == 'M' || matrice[upRightPos.first][upRightPos.second] == 'S')
        if (isUpRightValid) {
            val upRightChar = matrice[upRightPos.first][upRightPos.second]
            val expectedDownLeftChar = if (upRightChar == 'M') 'S' else 'M'

            upRightToBottomLeftIsOk = downLeftPos.first in matrice.indices &&
                    downLeftPos.second in matrice[downLeftPos.first].indices &&
                    matrice[downLeftPos.first][downLeftPos.second] == expectedDownLeftChar
        }

        // 4. Same with the upLeft -> bottomRight
        val isUpLeftValid = upLeftPos.first in matrice.indices &&
                upLeftPos.second in matrice[upLeftPos.first].indices &&
                (matrice[upLeftPos.first][upLeftPos.second] == 'M' || matrice[upLeftPos.first][upLeftPos.second] == 'S')

        if (isUpLeftValid) {
            val upLeftChar = matrice[upLeftPos.first][upLeftPos.second]
            val expectedDownRightChar = if (upLeftChar == 'M') 'S' else 'M'
            upLeftTobottomRightIsOk = downRightPos.first in matrice.indices &&
                    downRightPos.second in matrice[downRightPos.first].indices &&
                    matrice[downRightPos.first][downRightPos.second] == expectedDownRightChar
        }

        return upRightToBottomLeftIsOk && upLeftTobottomRightIsOk
    }

    fun findXMAS(matrice: List<String>): Int {

        val numberRows = matrice.size;
        val numberCols = matrice[0].length
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
