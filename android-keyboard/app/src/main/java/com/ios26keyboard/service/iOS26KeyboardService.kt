package com.ios26keyboard.service

import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.ios26keyboard.model.KeyboardMode
import com.ios26keyboard.view.KeyboardView

class iOS26KeyboardService : InputMethodService(), KeyboardView.OnKeyPressedListener {

    private var keyboardView: KeyboardView? = null
    private var currentText = StringBuilder()

    override fun onCreateInputView(): View {
        keyboardView = KeyboardView(this).apply {
            setKeyListener(this@iOS26KeyboardService)
            
            val isDarkMode = resources.configuration.uiMode and 
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            setDarkMode(isDarkMode)
        }
        return keyboardView!!
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        currentText.clear()
        updateSuggestions()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isDarkMode = newConfig.uiMode and 
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        keyboardView?.setDarkMode(isDarkMode)
    }

    override fun onKeyPressed(key: String) {
        val inputConnection = currentInputConnection ?: return
        inputConnection.commitText(key, 1)
        currentText.append(key)
        updateSuggestions()
    }

    override fun onDeletePressed() {
        val inputConnection = currentInputConnection ?: return
        val selectedText = inputConnection.getSelectedText(0)
        
        if (TextUtils.isEmpty(selectedText)) {
            inputConnection.deleteSurroundingText(1, 0)
            if (currentText.isNotEmpty()) {
                currentText.deleteCharAt(currentText.length - 1)
            }
        } else {
            inputConnection.commitText("", 1)
            currentText.clear()
        }
        updateSuggestions()
    }

    override fun onEnterPressed() {
        val inputConnection = currentInputConnection ?: return
        val editorInfo = currentInputEditorInfo
        
        when (editorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION)) {
            EditorInfo.IME_ACTION_SEARCH -> {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_SEARCH)
            }
            EditorInfo.IME_ACTION_SEND -> {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_SEND)
            }
            EditorInfo.IME_ACTION_GO -> {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_GO)
            }
            EditorInfo.IME_ACTION_NEXT -> {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_NEXT)
            }
            EditorInfo.IME_ACTION_DONE -> {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_DONE)
            }
            else -> {
                inputConnection.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
                )
                inputConnection.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER)
                )
            }
        }
        currentText.clear()
        updateSuggestions()
    }

    override fun onSpacePressed() {
        val inputConnection = currentInputConnection ?: return
        inputConnection.commitText(" ", 1)
        currentText.append(" ")
        updateSuggestions()
    }

    override fun onShiftPressed() {
    }

    override fun onModeChange(mode: KeyboardMode) {
    }

    override fun onLanguageSwitch() {
        val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imeManager.showInputMethodPicker()
    }

    override fun onEmojiPressed() {
        requestShowSelf(0)
    }

    override fun onMicPressed() {
        val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        
        try {
            startActivity(intent)
        } catch (e: Exception) {
        }
    }

    private fun updateSuggestions() {
        val suggestions = generateSuggestions(currentText.toString())
        keyboardView?.updateSuggestions(suggestions)
    }

    private fun generateSuggestions(text: String): List<String> {
        if (text.isEmpty()) {
            return listOf("I", "The", "Hello")
        }

        val lastWord = text.split(" ").lastOrNull()?.lowercase() ?: ""
        
        return when {
            lastWord.startsWith("hel") -> listOf("Hello", "Help", "Hello!")
            lastWord.startsWith("tha") -> listOf("Thanks", "That", "Thank you")
            lastWord.startsWith("wha") -> listOf("What", "What's", "Whatever")
            lastWord.startsWith("how") -> listOf("How", "How's", "However")
            lastWord.startsWith("the") -> listOf("The", "They", "There")
            lastWord.startsWith("go") -> listOf("Good", "Going", "Got")
            else -> listOf("the", "and", "to")
        }
    }
}
