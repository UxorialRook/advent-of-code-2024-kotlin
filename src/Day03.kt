import kotlin.math.abs

fun main() {

    /**
     * Simple multiplication without the do and don't patterns (part 1)
     */
    fun doTheMultiplication(value: String) : Int {
        return Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(value).map { match ->
            val (num1, num2) = match.destructured
            num1.toInt() * num2.toInt()
        }.sum()

    }

    /**
     * Multiplication that take into consideration do and don't pattern
     */
    fun doTheMultiplicationWithSwitchOnOff(value: String) : Int {
        val mulRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        val doRegex = Regex("""do\(\)""")
        val dontRegex = Regex("""don't\(\)""")

        var mulEnabled = true;
        var total = 0;

        // We are splitting the text in token, each one contains a do a don't or a mul instruction
        val tokens = value.split(Regex("""(?=do\(\)|don't\(\)|mul\()"""))
        for(token in tokens) {
            when {
                doRegex.containsMatchIn(token) -> mulEnabled = true
                dontRegex.containsMatchIn(token) -> mulEnabled = false
                mulRegex.containsMatchIn(token) -> {
                    if (mulEnabled) {
                        val match = mulRegex.find(token)
                        if (match != null) {
                            val (num1, num2) = match.destructured
                            total += num1.toInt() * num2.toInt()
                        }
                    }
                }
            }
        }
        return total;
    }

    fun part1(input: List<String>): Int {
        var result = 0;
        input.iterator().forEach {
            result += doTheMultiplication(it)
        }

        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0;
        var value = "";
        input.iterator().forEach {
            value += it;
        }
        result += doTheMultiplicationWithSwitchOnOff(value)
        println("");


        return result    }

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
