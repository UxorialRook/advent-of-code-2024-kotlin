fun main() {

    fun multiplyElements(list: List<Long>): Long {
        return list.reduce { acc, element -> acc * element }
    }

    fun findNumberPositionsOperator(numbers: List<Long>, result: Long, operators: List<(Long, Long) -> Long>) : Int
    {
        var total : Long = 0

        // We are storing all intermediate results in a map to create an tree with each root being
        // the previous result we are based on:
        // Ex: 12 -> [12 * 2, 12 / 2, 12 || 2]
        // The key of the map being the index (step number)
        val intermediateResults = mutableMapOf<Int, List<Long>>()
        numbers.forEachIndexed { index, number ->
            if(index == 0) {
                intermediateResults.put(index, mutableListOf(number))
            } else {
                val operatorsResult : MutableList<Long> = mutableListOf()
                operators.forEach { operator ->
                    intermediateResults[index-1]?.forEach { intermediate ->
                        operatorsResult.add(operator(intermediate, number))
                    }
                }
                intermediateResults[index] = operatorsResult.toList()
            }
        }

        return intermediateResults[numbers.size - 1]?.count { it == result } ?: 0
    }

    /**
     * Helper to split the input into a result and a List of Long
     * We need Long instead of Int because the number can be very big
     */
    fun parseLine(line: String): Pair<Long, List<Long>> {
        val (firstPart, secondPart) = line.split(":")
        val number = firstPart.trim().toLong()
        val numbersList = secondPart.trim()
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        return Pair(number, numbersList)
    }

    fun part1(input: List<String>): Long {
        var total : Long = 0
        // All operators possible
        val operators: List<(Long, Long) -> Long> = listOf(
            { a, b -> a * b },  // Multiplication
            { a, b -> a + b }   // Addition
        )
        input.forEach { line ->
            val (res, numbers) = parseLine(line)
            if(findNumberPositionsOperator(numbers, res, operators) > 0) {
                total += res
                print(" + $res ($total)")

            }
        }
        println()

        return total
    }

    fun part2(input: List<String>): Long {
        var total : Long = 0
        val operators: List<(Long, Long) -> Long> = listOf(
            { a, b -> a * b },  // Multiplication
            { a, b -> (a.toString() + b.toString()).toLong() },  // ||
            { a, b -> a + b }   // Addition
        )
        input.forEach { line ->
            val (res, numbers) = parseLine(line)
            if(findNumberPositionsOperator(numbers, res, operators) > 0) {
                total += res
                print(" + $res ($total)")

            }
        }
        println()

        return total
    }

        // Read the input from the `src/Day07.txt` file.
        val input = readInput("Day07")
        part1(input).println()
        part2(input).println()
}