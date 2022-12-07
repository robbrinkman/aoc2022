import java.io.File

fun main() {
//    AOC().exercise01()
//    AOC().exercise02()
//    AOC().exercise03()
//    AOC().exercise04()
//    AOC().exercise05()
//    AOC().exercise06()
//    AOC().exercise07()
    AOC().exercise08()
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
        println("Top three calories total: ${result.sortedBy { it }.reversed().subList(0, 3).sum()}")

    }

    fun exercise02() {
        val score = readInputFile("/02/example.txt").readLines().sumOf { line: String ->
            val (opponentHand, myHand) = readHands(line)
            val result = this.play(opponentHand, myHand)
            myHand + result
        }
        println("Total score: $score")

        val score2 = readInputFile("/02/example.txt").readLines().sumOf { line: String ->
            val (opponentHand, expectedResult) = readHands(line)
            val myHand = when (expectedResult) {
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

    private fun readHands(line: String): Pair<Int, Int> {
        val parts = line.split(" ")
        val opponentHand = parts[0][0].code - 'A'.code + 1
        val myHand = parts[1][0].code - 'X'.code + 1
        return Pair(opponentHand, myHand)
    }

    private fun play(opponentHand: Int, myHand: Int): Int {
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

    private fun priority(char: Char): Int {
        return when (char) {
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

    private fun parseCleanupAssignment(line: String): Pair<IntRange, IntRange> {
        val match = "^(\\d+)-(\\d+),(\\d+)-(\\d+)$".toRegex().find(line)
        val elf1Range = match!!.groups[1]!!.value.toInt()..match.groups[2]!!.value.toInt()
        val elf2Range = match.groups[3]!!.value.toInt()..match.groups[4]!!.value.toInt()
        return Pair(elf1Range, elf2Range)
    }

    fun exercise05() {

        val crateSize = 9

        val crates: MutableList<StackWithList> = mutableListOf()
        repeat(crateSize) { crates.add(StackWithList()) }

        readInputFile("/05/input.txt")
            .readLines()
            .filter { it.contains("[") }
            .reversed()
            .forEach {
                println(it)
                (crates.indices).forEach { i ->
                    val pos = (i * 4) + 1
                    if (pos < it.length) {
                        val char = it[pos]
                        if (char.isLetter()) {
                            crates[i].push(char)
                        }
                    }
                }
            }

        val instructions = readInputFile("/05/input.txt")
            .readLines()
            .filter { it.contains("move") }.map {
                "^move (\\d+) from (\\d+) to (\\d+)$".toRegex().find(it)
            }.map {
                Move(it!!.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt(), it.groups[3]!!.value.toInt())
            }

//        // 9000
//        instructions.forEach { instruction ->
//            repeat((0 until instruction.count).count()) {
//                if (crates[instruction.from - 1].peek() != null) {
//                    val item = crates[instruction.from - 1].pop() as Char
//                    crates[instruction.to - 1].push(item)
//                }
//            }
//        }
//
//        crates.forEach {
//            crate -> println(crate.peek())
//        }
//
//        val result = crates.map { it.peek()}.joinToString("")
//        println("9000: $result")

        // 9001
        instructions.forEach { instruction ->

            /*
            repeat(instruction.count) {
                if (crates[instruction.from - 1].peek() != null) {
                    val item = crates[instruction.from - 1].pop() as Char
                    crates[instruction.to - 1].push(item)
                }
            }*/

            if (instruction.count == 1) {
                println("Single")
                crates[instruction.to - 1].push(crates[instruction.from - 1].pop() as Char)
            } else if (instruction.count > 1) {
                val items: MutableList<Char> = mutableListOf()
                repeat(instruction.count) { _ ->
                    items.add(crates[instruction.from - 1].pop() as Char)
                }
                items.reversed().forEach { crates[instruction.to - 1].push(it) }
            } else {
                println("Should not happen")
            }

        }

        crates.forEach { crate ->
            println(crate.peek())
        }

        val result = crates.map { it.peek() }.joinToString("")
        println("9001: $result")

    }

    data class Move(val count: Int, val from: Int, val to: Int)

    class StackWithList {
        private val elements: MutableList<Any> = mutableListOf()

        private fun isEmpty() = elements.isEmpty()

        fun size() = elements.size

        fun push(item: Any) {
            elements.add(item)
        }

        fun pop(): Any? {
            val item = elements.lastOrNull()
            if (!isEmpty()) {
                elements.removeAt(elements.size - 1)
            }
            return item
        }

        fun peek(): Any? = elements.lastOrNull()

        override fun toString(): String = elements.toString()
    }

    fun exercise06() {
        // four characters that are all different
        val example = mapOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 7,
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
            "nppdvjthqldpwncqszvftbrmjlhg" to 6,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11
        )

//        example.forEach { (dataStream, markerPosition) ->
//            println("${findMarker(dataStream)} $markerPosition : ${findMarker(dataStream)==markerPosition}")
//            println("")
//            println("")
//        }

        val input =
            "qhbhzbzzfrzrbzzcjzjrrvcvrvqvvnggnngcgssswbblplrlflfnnnmmjppgddqndnrnlnccpfcfjcjvjdjqqqmhhmwhwmmsnsvsjvjnvjnvjvsjsmjsjccwcqwcqwqjqwjwmwbmmbzbsbvsslbsbbbntnvvphpqqvrrtbrtrfftppbggpzzfhfcfsfmssffmbmzzmqzzblzzzmwwnggjwgjwgjgpgmmjvvmcvmmcfchfhllwmlljqqldqdqttsgsvscsmsnsmstmtssvgsgddwdffbppwfpplhlchhhdvvdrrmttmptmmmjsmshmmmgqmgggzjgzzmwzwcwhchqqfpfvvbqvbqbrblrrmtmstmmjvmmdnmmzczdzpztppjhjjwzjjjtdjjpljpjcppjllsffhbffbhhgttqjqzzfzbzcbzcbcrrjjrwrgwwbcbpcccctrtqtfqqfjjpgpdgdfgfrggpjjljglgclcqcqmcqmmgjjllpmphpjjgfjjqrrbppwmpmccftctjtjgjtgggzffcggwzzzdjdzzlgzgjzzvqvppczzjnjvvfhhtwtttdwtdtvddpzpnpcnppmvmcmcsmstthctchcggtssdttvztvvldlfftqqbzzjttvzztppscctzccgmcmvmhhchcscbbshbssgwwthwhmwwcgwcwrrvrzvrzzzvhvdvmmprrdmrmfmrmbmjbbmqbmbqqhbbszsjjlqjljtjstshhgphpffdhhtggtgbbqcqgqccfffcpcbpbfppwqpqcclbbwdwsscpchhfpfmpfmflfnnmggwrrznnghgvhhghrrhwrrcschcscqcmcfcvvgzztjtqjjshspsqsmmjnnmttsshvhmmqfqzztbzttvhttmwttnqnfncfcpfflmllmtlmmphmhlmmltmtztcczhzbbfmmlglnnfpppqplljwjfwfdwdzwddszddqzqnzzzwwlzzqvvjlllrwlrrmpmrmbmpplpspqsqmqcmcjjshsvstvvwtvthvvrfvvqmmjpmjmrjmmlvvnnrjjrcrwrhwwqzzvgvngncgcqqcffmfzfssbnbfblbggwhggmtgtvvqhhpttbcbczcjjbqbhqbhqbbccbhbqhbbmppdlpdllbvvdpvdvwvsvppllgblbttmcmtccbsswmswwwzfwfhhtfhthctchcfhhhfjjvhjhgjjjcwjwggrtgrttcqcwcswccfdffvpvtptprrvjjqvjjghggshhwmmcscmsmhmvmppprfrwwrhhvghhtnhncctbbbwzzbgzgdzgdzdpdvpvbbwgbwwrqwrqwqbqvvclccfcfzfdfrrthrrqcqddplpqlppbbfrfmmrmnnwhhgddmwwrzrsswpwhwdwhhsmhshqsqllbvlbllwbwbpwwfwmwsmspsvsdsbscctpctppvvpggtjjdmdqqgqbgglccvzcvcnczcgzgmzznpzzpcpnpvpcvvffrttqrqttflfbfjfnnwnlwlhwwqzqnqfftstdsttglldwwgqwwvqqzczfzdffbfsfssfwswdwnwdnnbcncwctttvsvjsvsrvvbtvvzhhvjvtvtjtsjjvhjjwpjjnzzpczzppgcpgcpgcgsgvsgvsscrcpcpspllzvzddpssssdpsddhffllzmzhzfhhdvhhvbvwwpwqpwqpwwmvwmvwvgvmvpvmpmrmzrrblltjtggvnggvppthhzjhzhffrvrhvrhrlrslsflfhhtvhvmmhppjgpjpcccmqcqvqhvvfssrtmnwjjslwhjgpvrwspjlwdwrmvfgwmplrmjrllndrjzvjfbwvzpjpfqrnjspwcpsgcvdlmfdfrvwdcvmbrnzncgnqlcvgqtpsbbpvprncdsgvpqbpcnffwqmmfsvnzspchhrlnzbhcdfdgtsllmqfbrcqwbmmzrfvsghjpmrndsdbqvtprmblnbvbnpvhtphbpjwdssvwgdzwztbpzdcsqzldjzrgcwhhspblrtncvntppcgttlflflnntcnzpbpgsclcjvbjhldcdzwjjhnfwzjmgcwtljhvbncwqnjhbrhfqcmnsdvntsbgnpqttzvbhzzpdznrhjpnsqzsztsblstbghlpwbmqjctlnqnttwshfvmjdhgbgjdhbzrfjqndrrhlqcmplczjtwpstlsmwwzqzmgvhsvjgbrtfwmvwlbhpccbqvmfmlgmbmbmldbcwmmhpnnbnffbnqgwhclgpzgbpjqvzmqhhhpltnwrdfrrnmlfrzflpnjztlnfzzzgmncprtblpsvrqgrnzbzfzhzhjjjdrnpvjpnwmlmlgvvtqmdvpnhvcrdmthcnnnvhnzmvgrtdvcthgjtvcgmtpsvmfztrflrrzbmcfhftwwcnjfpjtsnzjccmvdnrrwvbfjgcjttdvzncqhlqqphwphclztbhlqcfmnhcjmsscplnrsjqpdzrrzbthbcdnrzgdmstpgqqsvzclvmzjjdfqhhhttwcjtmwcbltghmslqvltqbjqqjpjvgntvnlttjcnhltflglgsmjwjjfldpfgjgrhttbwfhpsdbsmsfmfbtjlnhvjfqjrqhwdrcwpfthdgqzjjjfcvgdffrhvvwzfghpszmjjgscjvjnlgnbfbgfrbbzbzbnzngthrddfmsgsqqdddpfqwlchfblrvjdcgnzfzwmmnmvnzmpfmhbbhsbfdfclzcnbrlgpbsvfgfpshrpvpgccmmghphrcvzwnlqjcfwrtwvlvcsdldldvnpwgrcsqlftllcctnvcwbdswvqlzwzzbpmvvctcrgnjfstbqvnzczrjlljfqzrwtfwmlvvdfbfntrrljtbrtbdfsqpnppfbppbsmghbnqddhrvwmgzttnqjrqlfrdhqjndmnjlbctgclltmznmrqtfjsjwnztdvhnhlfwpnnqlhhsrfzglsnrdnfvrqssbtlthzfnjdvrcgzsbnpdgqhhrlwspfqfqpvzdfwgrlhwplzvbzprsqzcwvhggvzpgjztnvwvddsflgsvqljmmhhdzqsqmthwzvllqwmsnvdpdbjcgdtrsnmwhnzhbhgjssstmhrpssnhnntmrbbbjgmjqtncbdljcgtmbctpgdrnqcnrpssrdtpbsmlzlcztbrggglswnjzqgbsmgbqdzppqrwgtnlrjrvlpnqlcdwhltzzlqdwwrglldzcqrjtjtlgdqrtwzjgtdthsdccsmsrbjjsgdqcwdltvnjwtddsnpnsvzcdbfqnvsjbngqrztmbrnbvhhjzdtqrgldpvjqjpnshbjdsdgbjdjzdmrvzhwmtgcjrfnprstqgfgnwfpcjzhlnwpdbtqbspssqdrzhmmsrqtlwngvbrvgdgztnrlwcnqwvcdmhhdrmpfqbgbjpvzwbsbgcpsnpjplcrjdhflqvsdctclqqnmprngtvbmlmpqrsqdsrzgsmzmsczpsnmfmtfnjvnddjhqbjdvtgftjfvjhgpjqdhlszqjmcbnwrppzwjvmgblspjmfhjdbnmrllnfqlpcbndvqdzhhmmrpsljgdshpnrgnmwfjsdncqcwlctccrqghfdbsqqbnwctcqpvlrqqqvdjwlcnzmvdmcvlwnftjnqqldfwhmdtcpnlgfcdjdrfvmwqdzsjzctmmmrswhlwthttvcsqqscdcsmjgqfjhswlpsfjrppdmbwrthcwszqwwgnjsdqdrswmnzbrvqcwlrlwwvjmrrhsnzprggbzhhdqwvnspsmzzqdtbphzvwrzvqnbntjndrwllzwchczdwvnfjjdwfhdlgncftldzwdtjzjrmnfwwgmqdrltmgrfsjztfcvwjsggtvbnsvthflwfdtljrgqhmfqhmhfffqhtgwtlmwgzsglqnfwnrnvgvbdgqjrqtsmgsmzdpffnnzwlpbqphqmgdzspfrdqlptwmfwlgnqqdhtbbjtfhllrhhdcszjtmrprzhzzlgjqbcnhzcmhzrsnmmrzztffrldthhfvwhgjhwmjfbdvnllfmlpdsldjnpcwlpbwqzdwbgjb\n"

        println("${findMarker(input)}")

    }

    private fun findMarker(dataStream: String): Int {
        var markers: MutableList<Char> = mutableListOf()
        dataStream.forEachIndexed { i, char ->
            println("$markers -> $char")

            if (markers.contains(char)) {
                println("REMOVE")
//                markers.removeAt(0)
                markers = markers.subList(markers.indexOf(char) + 1, markers.size)
                markers.add(char)
            } else {
                markers.add(char)
            }

            if (markers.size == 4) {
                return i + 1
            }
        }
        return -1
    }


    fun exercise07() {

        val rootDirectory = Directory(null, "/")

        var currentDirectory = rootDirectory

        readInputFile("/07/input.txt").forEachLine { line: String ->

            // Handle change directory
            if (line.startsWith("$ cd")) {
                val folder = line.substring(5)
                if (folder == "/") {
                    currentDirectory = rootDirectory
                } else if (folder == "..") {
                    if (currentDirectory.parentDirectory != null) {
                        currentDirectory = currentDirectory.parentDirectory!!
                    }
                } else {
                    var childDirectory = currentDirectory.childDirectories.find { it.name == folder }
                    if (childDirectory == null) {
                        childDirectory = Directory(currentDirectory, folder)
                        currentDirectory.childDirectories.add(childDirectory)
                    }
                    currentDirectory = childDirectory
                }
            } else if (line.startsWith("$ ls") || line.startsWith("dir")) {
                // Do Nothing for now
            } else {
                val splitResult = line.split(" ")
                val size = splitResult[0].toInt()
                val name = splitResult[1]
                currentDirectory.files[name] = size
            }
        }

        printDirectory(rootDirectory)

        val allDirectories: MutableList<Directory> = mutableListOf()
        addDirectory(allDirectories, rootDirectory)

        val result1 = allDirectories.filter { it.size() <= 100000 }.sumOf { it.size() }
        println("Sum: $result1")

        // Part 2
        val totalSize = 70000000
        val unusedSpace = totalSize - rootDirectory.size()
        val minimalSpaceNeeded = 30000000 - unusedSpace

        println("Unused space: $unusedSpace, space needed: $minimalSpaceNeeded")

        val result2 = allDirectories
            .filter { it.size() >= minimalSpaceNeeded }.minByOrNull { it.size() }!!

        println("Smallest directory to remove ${result2.name} -> ${result2.size()}")
    }

    private fun addDirectory(allDirectories: MutableList<Directory>, directory: Directory) {
        allDirectories.add(directory)
        directory.childDirectories.forEach { addDirectory(allDirectories, it) }
    }


    private fun printDirectory(dir: Directory) {
        println("Directory ${dir.name} (${dir.size()})")
        println("Files (${dir.files.size}):")
        dir.files.forEach { println("\t${it.key} (${it.value})") }
        println("Directories (${dir.childDirectories.size}):")
        dir.childDirectories.forEach { printDirectory(it) }
    }

    data class Directory(
        val parentDirectory: Directory?,
        val name: String,
        val files: MutableMap<String, Int> = mutableMapOf(),
        val childDirectories: MutableList<Directory> = mutableListOf()
    ) {
        fun size(): Int = files.values.sum() + childDirectories.sumOf { it.size() }

        override fun toString(): String {
            return "${this.name} : ${this.size()}"
        }

    }

    fun exercise08() {

        readInputFile("/08/example.txt")
    }


    private fun readInputFile(inputFile: String): File {
        return File(javaClass::class.java.getResource(inputFile).path)
    }


}

