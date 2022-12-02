import java.io.File

fun main(args: Array<String>) {
    AOC().exercise01()
}

class AOC {

    data class Elf(val calories: Int)

    fun exercise01() {


        val elfs : MutableList<Elf> = mutableListOf()
        var calorieCount =0;

        // Turn into nice code
        readInputFile("/01/calories_example.txt").forEachLine { line ->
            if (line.trim() == "") {
                elfs.add(Elf(calorieCount))
                calorieCount = 0;
            } else {
                calorieCount+= line.toInt()
            }
        }

        println(readInputFile("/01/input.txt").readLines().groupingBy { it == "" })

        println("Most calories: ${findElfWithMostCalories(elfs)?.calories}")
        println("Top three on calories: ${findTopThreeElves(elfs)}")
        println("Top three calories total: ${findTopThreeElves(elfs).sumOf { it.calories }}")
    }

    private fun findElfWithMostCalories(elfs : List<Elf>) = elfs.maxByOrNull { it.calories }

    private fun findTopThreeElves(elfs : List<Elf>) = elfs.sortedBy { it.calories }.reversed().subList(0,3)


    private fun readInputFile(inputFile: String) : File {
        return File(javaClass::class.java.getResource(inputFile).path)
    }
}

