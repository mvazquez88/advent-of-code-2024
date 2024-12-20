enum class Facing(val id: Char, val move: (P2) -> P2) {
    Right('>', { it.right(1) }),
    Down('v', { it.down(1) }),
    Left('<', { it.left(1) }),
    Up('^', { it.up(1) });

    fun next(): Facing = entries[(ordinal + 1) % entries.size]
    fun previous(): Facing = entries[Math.floorMod(ordinal - 1, entries.size)]

    val isHorizontal: Boolean = ordinal == 0 || ordinal == 2
    val isVertical: Boolean = ordinal == 1 || ordinal == 3

    companion object {
        fun fromChar(char: Char): Facing? = entries.firstOrNull { it.id == char }
    }
}
