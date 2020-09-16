package minesweeper

object IO {
    private const val PROMPT_NUMBER_OF_MINES = "How many mines do you want on the field? > "
    private const val PROMPT_MOVE = "Set/unset mines or claim a cell as free: > "
    private const val ERROR_INPUT = "Enter correct command: <x> <y> <free>|<mine>"
    const val ERROR_CELL_MARKING = "You cannot mark an empty cell!"
    const val MESSAGE_WIN = "Congratulations! You found all the mines!"
    const val MESSAGE_LOSE = "You stepped on a mine and failed!"
    private const val REGEX_VALID_USER_INPUT = """^.*[\dA-F]\s+[\dA-F]\s+(FREE|MINE).*$"""

    fun readNumberOfMines(mineField: MineField): Int {
        print(PROMPT_NUMBER_OF_MINES)
        val numberOfMines = readLine()?.toIntOrNull() ?: 0
        printField(mineField)
        return when {
            numberOfMines < 0 -> 0
            numberOfMines > mineField.height * mineField.width - 1 -> mineField.height * mineField.width - 1
            else -> numberOfMines
        }
    }

    fun getCoordinatesFromUser(mineField: MineField): Triple<Int, Int, String>? {
        print(PROMPT_MOVE)
        val inputString = readLine()?.trim()?.toUpperCase() ?: ""
        if (!inputString.matches(Regex(REGEX_VALID_USER_INPUT))) {
            println(ERROR_INPUT)
            return null
        }

        val command = inputString.split(" ")
        val x = command[0].toInt(16) - 1
        val y = command[1].toInt(16) - 1
        return if (x in 0 until mineField.width && y in 0 until mineField.height) Triple(x, y, command[2]) else null
    }

    fun printField(mineField: MineField) {
        val rows = mutableListOf<String>()
        rows.add("\n |")
        for (i in 1..mineField.width) {
            rows.add(Integer.toHexString(i).toUpperCase())
        }
        rows.add("|\n-|" + "-".repeat(mineField.width) + "|\n")
        for (i in mineField.cells.indices) {
            rows.add("${Integer.toHexString(i + 1).toUpperCase()}|")
            for (j in mineField.cells[i].indices) {
                rows.add(mineField.cells[i][j].image)
            }
            rows.add("|\n")
        }
        rows.add("-|" + "-".repeat(mineField.width) + "|\n")

        print(rows.joinToString(""))
    }

    fun output(message: String) {
        println(message)
    }
}