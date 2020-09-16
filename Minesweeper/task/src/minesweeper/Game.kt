package minesweeper

class Game {
    private val mineField = MineField()
    private val totalMines = IO.readNumberOfMines(mineField)
    private var isRunning = true
    private var firstReveal = true
    private var markedMines = 0
    private var markedCells = 0
    private var revealedCells = 0
    private var steppedOnMine = false
    private val status: Status
        get() {
            return when {
                markedCells == markedMines && markedMines == totalMines -> Status.WIN
                revealedCells == mineField.width * mineField.height - totalMines -> Status.WIN
                steppedOnMine -> Status.LOSE
                else -> Status.RUNNING
            }
        }

    enum class Status { RUNNING, WIN, LOSE }

    private fun placeMines(y: Int, x: Int) {
        mineField.placeMines(totalMines, Pair(y, x))
        mineField.generateHints()
    }

    fun run() {
        while (isRunning) {
            makeMove()
            IO.printField(mineField)
            checkGameStatus()
        }
    }

    private fun checkGameStatus() {
        isRunning = when (status) {
            Status.WIN -> {
                IO.output(IO.MESSAGE_WIN)
                false
            }
            Status.LOSE -> {
                IO.output(IO.MESSAGE_LOSE)
                false
            }
            else -> return
        }
    }

    private fun makeMove() {
        val (x, y, command) = IO.getCoordinatesFromUser(mineField) ?: return
        val cell = mineField.cells[y][x]

        when (command) {
            "MINE" -> {
                if (!cell.isHidden) {
                    IO.output(IO.ERROR_CELL_MARKING)
                    return
                }
                cell.mark()
                markedCells += if (cell.isMarked) 1 else -1
                markedMines += if (cell.isMarkedMine) 1 else 0
            }
            "FREE" -> {
                if (firstReveal) {
                    placeMines(y, x)
                    firstReveal = false
                }
                if (cell.isMine) {
                    steppedOnMine = true
                    mineField.revealAllMines()
                    return
                }
                revealedCells += mineField.reveal(y, x)
            }
            else -> return
        }
    }
}

