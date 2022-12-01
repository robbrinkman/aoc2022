import java.io.File

fun main(args: Array<String>) {
    AOC().exercise01()
}

class AOC {

    fun exercise01() {
        readInputFile("/01/calories_example.txt").forEachLine { line ->
            println(line)

        }
    }

    private fun readInputFile(inputFile: String) : File {
        return File(javaClass::class.java.getResource(inputFile).path)
    }
}

