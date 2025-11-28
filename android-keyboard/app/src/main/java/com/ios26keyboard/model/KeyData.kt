package com.ios26keyboard.model

data class KeyData(
    val primary: String,
    val secondary: String? = null,
    val keyCode: Int = 0,
    val isSpecial: Boolean = false,
    val width: Float = 1f
)

object KeyboardLayout {
    val qwertyRow1 = listOf(
        KeyData("Q", "1"),
        KeyData("W", "2"),
        KeyData("E", "3"),
        KeyData("R", "4"),
        KeyData("T", "5"),
        KeyData("Y", "6"),
        KeyData("U", "7"),
        KeyData("I", "8"),
        KeyData("O", "9"),
        KeyData("P", "0")
    )

    val qwertyRow2 = listOf(
        KeyData("A", "@"),
        KeyData("S", "#"),
        KeyData("D", "$"),
        KeyData("F", "%"),
        KeyData("G", "&"),
        KeyData("H", "*"),
        KeyData("J", "-"),
        KeyData("K", "+"),
        KeyData("L", "(")
    )

    val qwertyRow3 = listOf(
        KeyData("Z", "!"),
        KeyData("X", "\""),
        KeyData("C", "'"),
        KeyData("V", ":"),
        KeyData("B", ";"),
        KeyData("N", "/"),
        KeyData("M", "?")
    )

    val numbersRow1 = listOf(
        KeyData("1", ""),
        KeyData("2", ""),
        KeyData("3", ""),
        KeyData("4", ""),
        KeyData("5", ""),
        KeyData("6", ""),
        KeyData("7", ""),
        KeyData("8", ""),
        KeyData("9", ""),
        KeyData("0", "")
    )

    val numbersRow2 = listOf(
        KeyData("-", ""),
        KeyData("/", ""),
        KeyData(":", ""),
        KeyData(";", ""),
        KeyData("(", ""),
        KeyData(")", ""),
        KeyData("$", ""),
        KeyData("&", ""),
        KeyData("@", "")
    )

    val numbersRow3 = listOf(
        KeyData(".", ""),
        KeyData(",", ""),
        KeyData("?", ""),
        KeyData("!", ""),
        KeyData("'", "")
    )

    val symbolsRow1 = listOf(
        KeyData("[", ""),
        KeyData("]", ""),
        KeyData("{", ""),
        KeyData("}", ""),
        KeyData("#", ""),
        KeyData("%", ""),
        KeyData("^", ""),
        KeyData("*", ""),
        KeyData("+", ""),
        KeyData("=", "")
    )

    val symbolsRow2 = listOf(
        KeyData("_", ""),
        KeyData("\\", ""),
        KeyData("|", ""),
        KeyData("~", ""),
        KeyData("<", ""),
        KeyData(">", ""),
        KeyData("€", ""),
        KeyData("£", ""),
        KeyData("¥", "")
    )

    val symbolsRow3 = listOf(
        KeyData("•", ""),
        KeyData("©", ""),
        KeyData("®", ""),
        KeyData("™", ""),
        KeyData("°", "")
    )
}
