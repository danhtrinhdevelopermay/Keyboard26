package com.ios26keyboard.model

enum class KeyboardMode {
    LETTERS,
    NUMBERS,
    SYMBOLS
}

enum class ShiftState {
    OFF,
    ON,
    CAPS_LOCK
}

data class KeyboardState(
    var mode: KeyboardMode = KeyboardMode.LETTERS,
    var shiftState: ShiftState = ShiftState.OFF,
    var isDarkMode: Boolean = false,
    var isFloatingMode: Boolean = false,
    var currentLanguage: String = "vi",
    var isVietnameseMode: Boolean = true
)
