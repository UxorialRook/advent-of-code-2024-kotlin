import kotlin.math.abs

fun main() {

    fun isReportOkay(report: List<Int>): Boolean {
        val increasing = report.zipWithNext().all { (a, b) -> a < b }
        val decreasing = report.zipWithNext().all { (a, b) -> a > b }
        val diffBetween1And3 = report.zipWithNext().all { (a, b) -> abs(a - b) in 1..3 }

        return (increasing || decreasing) && diffBetween1And3

    }

    fun isSafe(report: List<Int>): Boolean {
        val isAlreadySafeAndIncreasing = report.zipWithNext().all { (a, b) -> b > a && abs(b - a) in 1..3 }
        val isAlreadySafeAndDecreasing = report.zipWithNext().all { (a, b) -> b < a && abs(b - a) in 1..3 }

        if (isAlreadySafeAndIncreasing || isAlreadySafeAndDecreasing) return true

        // For each indice in the report, we need to check if by removing it the constraints are now okay
        for (i in report.indices) {
            val modifiedReport = report.filterIndexed { index, _ -> index != i }
            val isSafeAndIncreasingAfterRemoval = modifiedReport.zipWithNext().all { (a, b) -> b > a && abs(b - a) in 1..3 }
            val isAlreadySafeAndDecreasingAfterRemoval = modifiedReport.zipWithNext().all { (a, b) -> b < a && abs(b - a) in 1..3 }
            if (isSafeAndIncreasingAfterRemoval || isAlreadySafeAndDecreasingAfterRemoval) return true
        }

        return false
    }

    fun part1(input: List<String>): Int {
        val report = mutableListOf<Int>();
        var result = 0;
        input.iterator().forEach {
            it.split(" ").forEach {
                report.add(it.toInt())
            }
            if(isReportOkay(report)) {
                result += 1
            }
            report.clear();
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val report = mutableListOf<Int>();
        var result = 0;
        input.iterator().forEach {
            it.split(" ").forEach {
                report.add(it.toInt())
            }
            if(isReportOkay(report)) {
                result += 1
            } else {
                if(isSafe(report)) {
                    result += 1
                }
            }
            report.clear();
        }

        return result;
    }

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
