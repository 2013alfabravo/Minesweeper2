package minesweeper

import kotlin.random.Random

class MineField(val height: Int = 9, val width: Int = 9) {
    val cells = Array(height)  { Array(width) { Cell() } }
    private val mines = mutableListOf<Pair<Int, Int>>()

    fun generateHints() {
        for (i in cells.indices) {
            for (j in cells[i].indices) {
                if (cells[i][j].isNotMine) {
                    cells[i][j].hint = getHint(i, j)
                }
            }
        }
    }

    private fun getHint(y: Int, x: Int): Int {
        var count = 0
        for (i in y - 1..y + 1) {
            for (j in x - 1..x + 1) {
                if (i in cells.indices && j in cells[i].indices && cells[i][j].isMine) {
                    count++
                }
            }
        }
        return count
    }

    fun placeMines(numberOfMines: Int, freeCell: Pair<Int, Int>) {
        val mineCoordinates = getMineCoordinates(numberOfMines, freeCell)
        for ((y, x) in mineCoordinates) {
            val markedCell = cells[y][x].isMarked
            cells[y][x] = Cell(true)
            if (markedCell) {
                cells[y][x].isMarked = true
            }
            mines.add(Pair(y, x))
        }
    }

    private fun getMineCoordinates(numberOfMines: Int, freeCell: Pair<Int, Int>): List<Pair<Int, Int>> {
        val list = mutableListOf<Int>()
        for (i in 0 until width * height) {
            list.add(i)
        }
        list.remove(freeCell.first * height + freeCell.second)

        val set = mutableListOf<Pair<Int, Int>>()
        repeat(numberOfMines) {
            val index = Random.nextInt(list.size)
            val cell = list[index]
            list.removeAt(index)
            set.add(Pair(cell / width, cell % height))
        }

        return set
    }

    fun reveal(y: Int, x: Int): Int {
        if (y !in 0 until height || x !in 0 until width || cells[y][x].isMine || !cells[y][x].isHidden) {
            return 0
        }

        cells[y][x].isHidden = false
        if (cells[y][x].hasHint) {
            return 1
        }

        return 1 + reveal(y - 1, x - 1) + reveal(y - 1, x) + reveal(y - 1, x + 1) +
                reveal(y, x - 1) + reveal(y, x + 1) +
                reveal(y + 1, x - 1) + reveal(y + 1, x) + reveal(y + 1, x + 1)
    }

    fun revealAllMines() {
        mines.forEach { cells[it.first][it.second].isHidden = false }
    }
}