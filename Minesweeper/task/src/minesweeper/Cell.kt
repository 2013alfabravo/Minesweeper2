package minesweeper

class Cell(val isMine: Boolean = false) {
    val isNotMine = !isMine
    var isMarked = false
    var isHidden = true
    var hint = 0
    val hasHint
        get() = hint != 0
    val isMarkedMine
        get() = isMarked && isMine

    val image
        get() = when {
            isMarked && isHidden -> MARKED
            isHidden -> HIDDEN
            hasHint -> hint.toString()
            isMine -> MINE
            else -> EMPTY
        }

    companion object {
        const val EMPTY = "/"
        const val HIDDEN = "."
        const val MINE = "X"
        const val MARKED = "*"
    }

    fun mark() {
        isMarked = !isMarked
    }
}