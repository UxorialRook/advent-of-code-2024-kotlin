enum class Direction(val delta: Pair<Int, Int>) {
        RIGHT(Pair(0, 1)),
        DOWN(Pair(1, 0)),
        LEFT(Pair(0, -1)),
        UP(Pair(-1, 0));

        fun do90DegreesTurn() : Direction {
            return when {
                this == UP -> RIGHT
                this == RIGHT -> DOWN
                this == DOWN -> LEFT
                this == LEFT -> UP
                else -> {
                    UP
                }
            }
        }

        fun displayPos(): String {
            return when {
                this == UP -> "^"
                this == RIGHT -> ">"
                this == DOWN -> "v"
                this == LEFT -> "<"
                else -> {"^"}
            }
        }
    }

    fun main() {

        /**
         * Generic method to find a specific char in a 2D matrice
         */
        fun findASign(matrice: List<String>, sign: Char) : Pair<Int, Int>? {
            return matrice.mapIndexedNotNull { row, line ->
                line.indexOfFirst { it == sign }.takeIf { it != -1 }?.let { row to it }
            }.firstOrNull()
        }

        fun findTheGuard(matrice: List<String>) : Pair<Int,Int>? {
            return findASign(matrice,'^')
        }

        fun isTheExit(matrice: List<String>, position: Pair<Int, Int>): Boolean {
            val (row, col) = position
            return row !in matrice.indices || col !in matrice.indices
        }

        fun hasObstacle(matrice: List<String>, position: Pair<Int, Int>): Boolean {
            val (row, col) = position
            val charToFind = '#'
            return row in matrice.indices && col in matrice[row].indices && matrice[row][col] == charToFind
        }

        /**
         * Second method for readibility, but we could merge it with hasObstacle
         */
        fun hasObstruction(matrice: List<String>, position: Pair<Int, Int>): Boolean {
            val (row, col) = position
            val charToFind = '0'
            return row in matrice.indices && col in matrice[row].indices && matrice[row][col] == charToFind
        }

        fun replaceCharAtPosition(matrice: List<String>, position: Pair<Int, Int>, newChar: Char) : List<String> {

            val newMatrice : MutableList<String> = matrice.toMutableList()
            val (x, y) = position

            if (x !in matrice.indices || y !in matrice[x].indices) {
                throw IndexOutOfBoundsException("Position ($x, $y) is out of bounds.")
            }

            val modifiedRow = StringBuilder(matrice[x]).apply {
                this[y] = newChar
            }.toString()

            newMatrice[x] = modifiedRow

            return newMatrice
        }

        fun printTheMatrice(matrice: MutableList<String>, guardPos: Pair<Pair<Int, Int>, Direction>, visited: MutableSet<Pair<Int, Int>> = mutableSetOf()) {
            val oldPosGuard = findTheGuard(matrice)
            if (oldPosGuard != null) {
               replaceCharAtPosition(matrice, oldPosGuard, '.')
            }
            matrice.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, col ->
                    if(guardPos.first.first == rowIndex && guardPos.first.second == colIndex) {
                        print(guardPos.second.displayPos())
                    } else if(visited.contains(Pair(rowIndex, colIndex))) {
                        print("X")
                    } else {
                        print(col)
                    }
                }
                println()
            }
        }

        fun moveGuard(matrice: List<String>, currentPos: Pair<Int, Int>, direction: Direction = Direction.UP): Pair<Pair<Int, Int>, Direction> {

            var currentDirection = direction

            // We could also do it recursively. The following code check the 4 positions to find the next move
            // if no move has been found, will return the currentPos with the currentDirection
            repeat(4) {
                val (dx, dy) = currentDirection.delta
                val nextPos = Pair(currentPos.first + dx, currentPos.second + dy)

                if (!hasObstacle(matrice, nextPos)) {
                    return Pair(nextPos, currentDirection)
                }

                currentDirection = currentDirection.do90DegreesTurn()
            }

            return Pair(currentPos, currentDirection)
        }

        fun isGuardStuck(matrice: List<String>, start: Pair<Int, Int>, startDirection: Direction = Direction.UP): Boolean {
            val visitedStates = mutableSetOf<Triple<Int, Int, Direction>>() // Track position + direction
            var position = start
            var direction = startDirection
            while (true) {
                // We store 3 elements to determine if the guard is stuck: the current position && the direction.
                // The position only is not enough, because the guard can visit the same spot multiple time
                val state = Triple(position.first, position.second, direction)

                if (!visitedStates.add(state)) {
                    return true
                }

                val (dx, dy) = direction.delta
                val nextPos = Pair(position.first + dx, position.second + dy)

                // To know if the player is blocked, we need to check
                // if the player encounters a classical obstacle or the obstruction we put
                if (hasObstacle(matrice, nextPos) || hasObstruction(matrice, nextPos)) {
                    direction = direction.do90DegreesTurn()
                } else if (isTheExit(matrice, nextPos)) {
                    return false
                } else {
                    position = nextPos
                }
            }
        }

        fun findTheExit(input: List<String>) : Int {
            val matrice = input.toMutableList()
            val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
            var guardPos: Pair<Int, Int>? = findTheGuard(matrice)
            if(guardPos == null) {
                throw Exception("Guard not found")
            }

            visited.add(guardPos)
            var nextPos = moveGuard(matrice, guardPos)
            visited.add(nextPos.first)
            while(!isTheExit(matrice, nextPos.first)) {
                nextPos = moveGuard(matrice, nextPos.first, nextPos.second)
                visited.add(nextPos.first)

                println(nextPos)
            }
            printTheMatrice(matrice, nextPos, visited)

            return visited.size - 1
        }


        /**
         * Return the number of positions where we can put an obstruction
         * that will make the player do an infinite loop.
         *
         * The algorithm is naive: foreach element on the matrice, we are trying
         * to put an obstruction and see what's happening
         */
        fun tryAllObstructions(input: List<String>) : Int {
            var matrice = input
            var lastPos = Pair(0,0)
            var count = 0;
            var guardPos: Pair<Int, Int>? = findTheGuard(matrice)
            if(guardPos == null) {
                throw Exception("Guard not found")
            }
            matrice.forEachIndexed { indexRow, row ->
                row.forEachIndexed { indexCol, col ->
                    if(matrice[lastPos.first][lastPos.second] == '0') {
                        matrice = replaceCharAtPosition(matrice, lastPos, '.')
                    }
                    if(matrice[indexRow][indexCol] == '.') {
                        lastPos = Pair(indexRow, indexCol)
                        matrice = replaceCharAtPosition(matrice, lastPos, '0')
                        if(isGuardStuck(matrice, guardPos, Direction.UP)) {
                            count++
                        }
                    }
                }
            }

            return count
        }

        fun part1(input: List<String>): Int {
            return findTheExit(input)
        }

        fun part2(input: List<String>): Int {
            return tryAllObstructions(input)
        }

        // Read the input from the `src/Day06.txt` file.
        val input = readInput("Day06")
        part1(input).println()
        part2(input).println()
    }
