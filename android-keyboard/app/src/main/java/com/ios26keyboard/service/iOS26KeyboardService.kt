package com.ios26keyboard.service

import android.content.Context
import android.content.res.Configuration
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import com.ios26keyboard.model.KeyboardMode
import com.ios26keyboard.utils.VietnameseInputProcessor
import com.ios26keyboard.utils.WordSuggestionHelper
import com.ios26keyboard.view.KeyboardView

class iOS26KeyboardService : InputMethodService(), KeyboardView.OnKeyPressedListener {

    private var keyboardView: KeyboardView? = null
    private var currentWord = StringBuilder()
    private var isVietnameseMode = true
    private val vietnameseProcessor = VietnameseInputProcessor()
    private val suggestionHelper = WordSuggestionHelper()
    
    companion object {
        private const val PREFS_NAME = "iOS26KeyboardPrefs"
        private const val KEY_LANGUAGE = "language_mode"
    }

    override fun onCreate() {
        super.onCreate()
        loadLanguagePreference()
    }
    
    private fun loadLanguagePreference() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isVietnameseMode = prefs.getBoolean(KEY_LANGUAGE, true)
    }
    
    private fun saveLanguagePreference() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LANGUAGE, isVietnameseMode).apply()
    }

    override fun onWindowShown() {
        super.onWindowShown()
        enableBackdropBlur()
        hideNavigationBar()
    }

    private fun enableBackdropBlur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window?.window?.let { window ->
                window.setBackgroundBlurRadius(80)
            }
        }
    }

    private fun hideNavigationBar() {
        window?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior = 
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
            }
        }
    }

    override fun onCreateInputView(): View {
        keyboardView = KeyboardView(this).apply {
            setKeyListener(this@iOS26KeyboardService)
            
            val isDarkMode = resources.configuration.uiMode and 
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            setDarkMode(isDarkMode)
            updateLanguageIndicator(isVietnameseMode)
        }
        return keyboardView!!
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        currentWord.clear()
        updateSuggestions()
        keyboardView?.updateLanguageIndicator(isVietnameseMode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isDarkMode = newConfig.uiMode and 
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        keyboardView?.setDarkMode(isDarkMode)
    }

    override fun onKeyPressed(key: String) {
        val inputConnection = currentInputConnection ?: return
        
        if (key.length != 1) {
            inputConnection.commitText(key, 1)
            currentWord.append(key)
            updateSuggestions()
            return
        }
        
        val char = key[0]
        
        if (isVietnameseMode && currentWord.isNotEmpty()) {
            val result = vietnameseProcessor.processKey(currentWord.toString(), char)
            
            if (result.isProcessed) {
                if (result.deleteCount > 0) {
                    inputConnection.deleteSurroundingText(result.deleteCount, 0)
                    repeat(result.deleteCount) {
                        if (currentWord.isNotEmpty()) {
                            currentWord.deleteCharAt(currentWord.length - 1)
                        }
                    }
                }
                inputConnection.commitText(result.text, 1)
                currentWord.append(result.text)
                updateSuggestions()
                return
            }
        }
        
        inputConnection.commitText(key, 1)
        currentWord.append(key)
        updateSuggestions()
    }

    override fun onDeletePressed() {
        val inputConnection = currentInputConnection ?: return
        val selectedText = inputConnection.getSelectedText(0)
        
        if (TextUtils.isEmpty(selectedText)) {
            inputConnection.deleteSurroundingText(1, 0)
            if (currentWord.isNotEmpty()) {
                currentWord.deleteCharAt(currentWord.length - 1)
            }
        } else {
            inputConnection.commitText("", 1)
            currentWord.clear()
        }
        updateSuggestions()
    }

    override fun onEnterPressed() {
        val inputConnection = currentInputConnection ?: return
        val editorInfo = currentInputEditorInfo
        
        currentWord.clear()
        
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
        updateSuggestions()
    }

    override fun onSpacePressed() {
        val inputConnection = currentInputConnection ?: return
        inputConnection.commitText(" ", 1)
        currentWord.clear()
        updateSuggestions()
    }

    override fun onShiftPressed() {
    }

    override fun onModeChange(mode: KeyboardMode) {
    }

    override fun onLanguageSwitch() {
        isVietnameseMode = !isVietnameseMode
        saveLanguagePreference()
        keyboardView?.updateLanguageIndicator(isVietnameseMode)
        
        val languageName = if (isVietnameseMode) "Tiếng Việt (Telex)" else "English"
        Toast.makeText(this, languageName, Toast.LENGTH_SHORT).show()
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
    
    override fun onSuggestionSelected(suggestion: String) {
        val inputConnection = currentInputConnection ?: return
        
        val currentWordLength = currentWord.length
        if (currentWordLength > 0) {
            inputConnection.deleteSurroundingText(currentWordLength, 0)
        }
        
        inputConnection.commitText(suggestion + " ", 1)
        
        currentWord.clear()
        updateSuggestions()
    }

    private fun updateSuggestions() {
        val suggestions = suggestionHelper.getSuggestions(
            currentWord.toString(),
            isVietnameseMode,
            3
        )
        keyboardView?.updateSuggestions(suggestions)
    }
}
