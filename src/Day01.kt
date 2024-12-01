import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val list1 = mutableListOf<Int>();
        val list2 = mutableListOf<Int>();
        input.iterator().forEach {
            val temp = it.split("   ");
            list1.add(temp[0].toInt())
            list2.add(temp[1].toInt())
        }

        return list1.sorted().zip(list2.sorted()) { elList1, elList2 ->
            abs(elList1 - elList2)
        }.sum()
    }

    fun part2(input: List<String>): Int {

        val list1 = mutableListOf<Int>();
        val list2 = mutableListOf<Int>();
        input.iterator().forEach {
            val temp = it.split("   ");
            list1.add(temp[0].toInt())
            list2.add(temp[1].toInt())
        }

        // mapResult if the map that we will reduce to find the final answer
        // mapNumberTime contains how many time we found the element in the left list for
        // the final calculation
        val mapResult = list1.associateWith {0}.toMutableMap()
        val mapNumberTime = list1.associateWith { 0 }.toMutableMap()

        list1.iterator().forEach {
            mapNumberTime[it] = (mapNumberTime[it] ?: 0) + 1
        }

        list2.iterator().forEach {
            if(mapResult.containsKey(it)) {
                mapResult[it] = (mapResult[it] ?: 0) + 1
            }
        }

        // key (the left value) * value (number time in the right) * number time in the left
        return mapResult.entries.map {
            it.key * it.value * (mapNumberTime[it.key] ?: 1)
        }.reduce { acc, value ->
            acc + value
        }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
