import java.io.File

fun main(args: Array<String>) {
    AOC().exercise01()
//    AOC().exercise02()
//    AOC().exercise03()
//    AOC().exercise04()
}

class AOC {



    fun exercise01() {

        val result = readInputFile("/01/example.txt")
            .readLines()
            .joinToString(",").split(",,")
            .map {
                it.split(",").sumOf { score -> score.toInt() }
            }

        println("Most calories: ${result.maxOrNull()}")
        println("Top three calories total: ${result.sortedBy { it }.reversed().subList(0,3).sum()}")

    }


    private fun findTopThreeElves(elfs : List<Int>) = elfs.sortedBy { it }.reversed().subList(0,3)


    fun exercise02() {
        val score = readInputFile("/02/example.txt").readLines().sumOf { line: String ->
            val (opponentHand, myHand) = readHands(line)
            val result = this.play(opponentHand, myHand)
            myHand + result
        }
        println("Total score: $score")


        val score2 = readInputFile("/02/example.txt").readLines().sumOf { line: String ->
            val (opponentHand, expectedResult) = readHands(line)
            val myHand = when(expectedResult) {
                // Lose
                1 -> if (opponentHand == 1) 3 else if (opponentHand == 2) 1 else 2
                // Win
                3 -> if (opponentHand == 1) 2 else if (opponentHand == 2) 3 else 1
                // Draw
                else -> opponentHand
            }
            val result = this.play(opponentHand, myHand)
            myHand + result
        }

        println("Total score2: $score2")
    }

    private fun readHands(line: String) : Pair<Int, Int> {
        val parts = line.split(" ")
        val opponentHand = parts[0][0].code - 'A'.code + 1
        val myHand = parts[1][0].code - 'X'.code + 1
        return Pair(opponentHand, myHand)
    }

    private fun play(opponentHand: Int, myHand: Int) : Int {
        return if (opponentHand == myHand) 3
        else if (opponentHand == 1 && myHand == 2) 6
        else if (opponentHand == 2 && myHand == 3) 6
        else if (opponentHand == 3 && myHand == 1) 6
        else 0
    }

    fun exercise03() {
        val result = readInputFile("/03/input.txt").readLines()
            .map { it.toList() }
            .map { it.chunked(it.size / 2) }
            .flatMap { it[0].intersect(it[1].toSet()) }
            .sumOf { priority(it) }

        println("Priority sum: $result")

        val result2 = readInputFile("/03/input.txt")
            .readLines()
            .chunked(3)
            .flatMap {
                // Determine which items are in each bag
                it[0].toList()
                    .intersect(it[1].toList().toSet()).toList()
                    .intersect(it[2].toList().toSet())
            }.sumOf { priority(it) }

        println("Priority sum of badges: $result2")
    }

    private fun priority(char : Char) : Int {
        return when(char) {
            in 'a'..'z' -> char.code - 96
            else -> char.code - 38
        }
    }


    fun exercise04() {
        val result = readInputFile("/04/example.txt")
            .readLines()
            .map {
                parseCleanupAssignment(it)
            }.count {
                it.first.first <= it.second.first && it.first.last >= it.second.last
                        || it.second.first <= it.first.first && it.second.last >= it.first.last
            }

        println("Number of pairs with overlapping work: $result")

        val result2 = readInputFile("/04/input.txt")
            .readLines().map {
                parseCleanupAssignment(it)
            }.count {
                it.first.intersect(it.second).isNotEmpty()
            }

        println("Number of assignments that overlap: $result2")
    }

    private fun parseCleanupAssignment(line : String): Pair<IntRange, IntRange> {
        val match = "^(\\d+)-(\\d+),(\\d+)-(\\d+)$".toRegex().find(line)
        val elf1Range = match!!.groups[1]!!.value.toInt()..match.groups[2]!!.value.toInt()
        val elf2Range = match.groups[3]!!.value.toInt()..match.groups[4]!!.value.toInt()
        return Pair(elf1Range, elf2Range)
    }



    private fun readInputFile(inputFile: String) : File {
        return File(javaClass::class.java.getResource(inputFile).path)
    }

}

