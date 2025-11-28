package com.ios26keyboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ios26keyboard.R
import com.ios26keyboard.model.*
import com.ios26keyboard.utils.HapticHelper
import com.ios26keyboard.utils.KeyAnimationHelper

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var keyboardState = KeyboardState()
    private var keyListener: OnKeyPressedListener? = null

    private lateinit var row1: LinearLayout
    private lateinit var row2: LinearLayout
    private lateinit var row3: LinearLayout
    private lateinit var row4: LinearLayout
    private lateinit var suggestionBar: LinearLayout
    private lateinit var btnEmoji: ImageView
    private lateinit var btnMic: ImageView

    interface OnKeyPressedListener {
        fun onKeyPressed(key: String)
        fun onDeletePressed()
        fun onEnterPressed()
        fun onSpacePressed()
        fun onShiftPressed()
        fun onModeChange(mode: KeyboardMode)
        fun onLanguageSwitch()
        fun onEmojiPressed()
        fun onMicPressed()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.keyboard_view, this, true)
        initializeViews()
        setupKeyboard()
    }

    private fun initializeViews() {
        row1 = findViewById(R.id.row_1)
        row2 = findViewById(R.id.row_2)
        row3 = findViewById(R.id.row_3)
        row4 = findViewById(R.id.row_4)
        suggestionBar = findViewById(R.id.suggestion_bar)
        btnEmoji = findViewById(R.id.btn_emoji)
        btnMic = findViewById(R.id.btn_mic)

        btnEmoji.setOnClickListener {
            HapticHelper.performKeyPressHaptic(it)
            keyListener?.onEmojiPressed()
        }

        btnMic.setOnClickListener {
            HapticHelper.performKeyPressHaptic(it)
            keyListener?.onMicPressed()
        }
    }

    private fun setupKeyboard() {
        when (keyboardState.mode) {
            KeyboardMode.LETTERS -> setupLettersKeyboard()
            KeyboardMode.NUMBERS -> setupNumbersKeyboard()
            KeyboardMode.SYMBOLS -> setupSymbolsKeyboard()
        }
    }

    private fun setupLettersKeyboard() {
        row1.removeAllViews()
        row2.removeAllViews()
        row3.removeAllViews()
        row4.removeAllViews()

        KeyboardLayout.qwertyRow1.forEach { keyData ->
            row1.addView(createKeyView(keyData))
        }

        KeyboardLayout.qwertyRow2.forEach { keyData ->
            row2.addView(createKeyView(keyData))
        }

        row3.addView(createSpecialKeyView("⇧", "shift", 1.5f))
        KeyboardLayout.qwertyRow3.forEach { keyData ->
            row3.addView(createKeyView(keyData))
        }
        row3.addView(createSpecialKeyView("⌫", "delete", 1.5f))

        row4.addView(createSpecialKeyView("123", "numbers", 1.2f))
        row4.addView(createSpecialKeyView("🌐", "globe", 1f))
        row4.addView(createSpaceKeyView())
        row4.addView(createSpecialKeyView("Search", "return", 2f))
    }

    private fun setupNumbersKeyboard() {
        row1.removeAllViews()
        row2.removeAllViews()
        row3.removeAllViews()
        row4.removeAllViews()

        KeyboardLayout.numbersRow1.forEach { keyData ->
            row1.addView(createKeyView(keyData))
        }

        KeyboardLayout.numbersRow2.forEach { keyData ->
            row2.addView(createKeyView(keyData))
        }

        row3.addView(createSpecialKeyView("#+=", "symbols", 1.5f))
        KeyboardLayout.numbersRow3.forEach { keyData ->
            row3.addView(createKeyView(keyData))
        }
        row3.addView(createSpecialKeyView("⌫", "delete", 1.5f))

        row4.addView(createSpecialKeyView("ABC", "letters", 1.2f))
        row4.addView(createSpecialKeyView("🌐", "globe", 1f))
        row4.addView(createSpaceKeyView())
        row4.addView(createSpecialKeyView("Return", "return", 2f))
    }

    private fun setupSymbolsKeyboard() {
        row1.removeAllViews()
        row2.removeAllViews()
        row3.removeAllViews()
        row4.removeAllViews()

        KeyboardLayout.symbolsRow1.forEach { keyData ->
            row1.addView(createKeyView(keyData))
        }

        KeyboardLayout.symbolsRow2.forEach { keyData ->
            row2.addView(createKeyView(keyData))
        }

        row3.addView(createSpecialKeyView("123", "numbers", 1.5f))
        KeyboardLayout.symbolsRow3.forEach { keyData ->
            row3.addView(createKeyView(keyData))
        }
        row3.addView(createSpecialKeyView("⌫", "delete", 1.5f))

        row4.addView(createSpecialKeyView("ABC", "letters", 1.2f))
        row4.addView(createSpecialKeyView("🌐", "globe", 1f))
        row4.addView(createSpaceKeyView())
        row4.addView(createSpecialKeyView("Return", "return", 2f))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createKeyView(keyData: KeyData): TextView {
        val keyboardView = this@KeyboardView
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                resources.getDimensionPixelSize(R.dimen.key_height)
            ).apply {
                weight = keyData.width
                setMargins(4, 4, 4, 4)
            }

            text = if (keyboardView.keyboardState.shiftState != ShiftState.OFF) 
                keyData.primary.uppercase() 
            else 
                keyData.primary.lowercase()
            
            textSize = 18f
            gravity = android.view.Gravity.CENTER
            setTextColor(keyboardView.getKeyTextColor())
            background = ContextCompat.getDrawable(context, R.drawable.key_background)
            elevation = 4f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        KeyAnimationHelper.animateKeyPress(view)
                        HapticHelper.performKeyPressHaptic(view)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        keyboardView.keyListener?.onKeyPressed(text.toString())
                        
                        if (keyboardView.keyboardState.shiftState == ShiftState.ON) {
                            keyboardView.keyboardState.shiftState = ShiftState.OFF
                            keyboardView.refreshKeyboard()
                        }
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createSpecialKeyView(label: String, action: String, weight: Float): TextView {
        val keyboardView = this@KeyboardView
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                resources.getDimensionPixelSize(R.dimen.key_height)
            ).apply {
                this.weight = weight
                setMargins(4, 4, 4, 4)
            }

            text = label
            textSize = if (label.length > 3) 14f else 16f
            gravity = android.view.Gravity.CENTER
            setTextColor(keyboardView.getKeyTextColor())
            background = ContextCompat.getDrawable(context, R.drawable.special_key_background)
            elevation = 4f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        KeyAnimationHelper.animateKeyPress(view)
                        HapticHelper.performKeyPressHaptic(view)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        keyboardView.handleSpecialKey(action)
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createSpaceKeyView(): TextView {
        val keyboardView = this@KeyboardView
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                resources.getDimensionPixelSize(R.dimen.key_height)
            ).apply {
                weight = 4f
                setMargins(4, 4, 4, 4)
            }

            text = "space"
            textSize = 14f
            gravity = android.view.Gravity.CENTER
            setTextColor(keyboardView.getKeyTextColor())
            background = ContextCompat.getDrawable(context, R.drawable.key_background)
            elevation = 4f
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)

            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        KeyAnimationHelper.animateKeyPress(view)
                        HapticHelper.performKeyPressHaptic(view)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        keyboardView.keyListener?.onSpacePressed()
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        KeyAnimationHelper.animateKeyRelease(view)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun handleSpecialKey(action: String) {
        when (action) {
            "shift" -> {
                keyboardState.shiftState = when (keyboardState.shiftState) {
                    ShiftState.OFF -> ShiftState.ON
                    ShiftState.ON -> ShiftState.CAPS_LOCK
                    ShiftState.CAPS_LOCK -> ShiftState.OFF
                }
                keyListener?.onShiftPressed()
                refreshKeyboard()
            }
            "delete" -> keyListener?.onDeletePressed()
            "return" -> keyListener?.onEnterPressed()
            "numbers" -> {
                keyboardState.mode = KeyboardMode.NUMBERS
                keyListener?.onModeChange(KeyboardMode.NUMBERS)
                refreshKeyboard()
            }
            "symbols" -> {
                keyboardState.mode = KeyboardMode.SYMBOLS
                keyListener?.onModeChange(KeyboardMode.SYMBOLS)
                refreshKeyboard()
            }
            "letters" -> {
                keyboardState.mode = KeyboardMode.LETTERS
                keyListener?.onModeChange(KeyboardMode.LETTERS)
                refreshKeyboard()
            }
            "globe" -> keyListener?.onLanguageSwitch()
        }
    }

    private fun refreshKeyboard() {
        KeyAnimationHelper.animateModeSwitch(this) {
            setupKeyboard()
        }
    }

    private fun getKeyTextColor(): Int {
        return ContextCompat.getColor(
            context,
            if (keyboardState.isDarkMode) R.color.key_text_dark else R.color.key_text_light
        )
    }

    fun setKeyListener(listener: OnKeyPressedListener) {
        keyListener = listener
    }

    fun setDarkMode(isDark: Boolean) {
        keyboardState.isDarkMode = isDark
        refreshKeyboard()
    }

    fun updateSuggestions(suggestions: List<String>) {
        val suggestion1 = suggestionBar.getChildAt(0) as? TextView
        val suggestion2 = suggestionBar.getChildAt(1) as? TextView
        val suggestion3 = suggestionBar.getChildAt(2) as? TextView

        suggestion1?.text = suggestions.getOrNull(0) ?: ""
        suggestion2?.text = suggestions.getOrNull(1) ?: ""
        suggestion3?.text = suggestions.getOrNull(2) ?: ""
    }
}
