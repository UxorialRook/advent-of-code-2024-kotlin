import kotlin.math.abs

fun main() {

    /**
     * Create a directed graph
     */
    fun createGraph(update: List<Int>, rules: List<Pair<Int, Int>>): Pair<MutableMap<Int, MutableList<Int>>, MutableMap<Int, Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()
        val indegree = mutableMapOf<Int, Int>()

        // 1. Each node is init with an empty adjacency list and an indegree of 0
        update.forEach { node ->
            graph[node] = mutableListOf()
            indegree[node] = 0
        }

        // 2. We process the rules to create the directed edges which will be very useful
        // after when we will need to sort the graph to know which node depend on which
        rules.forEach { (before, after) ->
            if (update.contains(before) && update.contains(after)) {
                graph[before]?.add(after)
                indegree[after] = indegree.getOrDefault(after, 0) + 1
            }
        }

        return graph to indegree
    }

    fun reorderUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {

        val (graph, indegree) = createGraph(update, rules)

        // Perform topological sort using Kahn's algorithm
        // CF: https://www.geeksforgeeks.org/topological-sorting-indegree-based-solution/
        val queue = ArrayDeque<Int>()
        val result = mutableListOf<Int>()

        // Add all nodes with 0 indegree (incoming edges) to the queue
        indegree.forEach { (node, degree) ->
            if (degree == 0) queue.add(node)
        }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result.add(current)

            graph[current]?.forEach { neighbor ->
                indegree[neighbor] = indegree[neighbor]!! - 1
                if (indegree[neighbor] == 0) {
                    queue.add(neighbor)
                }
            }
        }

        return result
    }

    /**
     * Simple method that check if a list of int if all elements come in the right order
     */
    fun isUpdateInCorrectOrder(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
        return rules.all { (before, after) ->
            val indexBefore = update.indexOf(before)
            val indexAfter = update.indexOf(after)

            indexBefore == -1 || indexAfter == -1 || indexBefore < indexAfter
        }
    }

    fun part1(input: List<String>): Int {

        val rules: MutableList<Pair<Int, Int>> = mutableListOf()
        var total = 0
        input.forEach() {
            if(it.contains("|")) {
                rules.add(it.split("|").let { it[0].toInt() to it[1].toInt() })
            } else if(it.isNotEmpty()){
                val elements = it.split(",").map { it.toInt() }.toMutableList()
                if(isUpdateInCorrectOrder(elements, rules)) {
                    total += elements[elements.size/2]
                }
                elements.clear()
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val rules: MutableList<Pair<Int, Int>> = mutableListOf()
        var total = 0

        input.forEach { line ->
            if (line.contains("|")) {
                rules.add(line.split("|").let { it[0].toInt() to it[1].toInt() })
            } else if (line.isNotEmpty()) {
                val elements = line.split(",").map { it.toInt() }.toMutableList()
                if (!isUpdateInCorrectOrder(elements, rules)) {
                    val reordered = reorderUpdate(elements, rules)
                    total += reordered[reordered.size /2];
                }
            }
        }

        return total
    }

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
