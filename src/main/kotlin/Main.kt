import java.io.File

fun main(args: Array<String>) {
    //AOC().exercise01()
    //AOC().exercise02()
    AOC().exercise03()
}

class AOC {

    data class Elf(val calories: Int)

    fun exercise01() {

        val elfs : MutableList<Elf> = mutableListOf()
        var calorieCount =0;

        // Turn into nice code
        readInputFile("/01/example.txt").forEachLine { line ->
            if (line.trim() == "") {
                elfs.add(Elf(calorieCount))
                calorieCount = 0;
            } else {
                calorieCount+= line.toInt()
            }
        }

        println("Most calories: ${findElfWithMostCalories(elfs)?.calories}")
        println("Top three on calories: ${findTopThreeElves(elfs)}")
        println("Top three calories total: ${findTopThreeElves(elfs).sumOf { it.calories }}")


    }

    private fun findElfWithMostCalories(elfs : List<Elf>) = elfs.maxByOrNull { it.calories }

    private fun findTopThreeElves(elfs : List<Elf>) = elfs.sortedBy { it.calories }.reversed().subList(0,3)


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


    private fun readInputFile(inputFile: String) : File {
        return File(javaClass::class.java.getResource(inputFile).path)
    }

    fun exercise03() {

    }
}

