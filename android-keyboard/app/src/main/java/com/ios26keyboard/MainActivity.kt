package com.ios26keyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        checkKeyboardStatus()
    }

    private fun setupViews() {
        findViewById<Button>(R.id.btn_enable_keyboard)?.setOnClickListener {
            openKeyboardSettings()
        }

        findViewById<Button>(R.id.btn_select_keyboard)?.setOnClickListener {
            showKeyboardPicker()
        }

        findViewById<Button>(R.id.btn_test_keyboard)?.setOnClickListener {
            val editText = findViewById<EditText>(R.id.edit_test)
            editText?.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun openKeyboardSettings() {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Không thể mở cài đặt bàn phím",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showKeyboardPicker() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showInputMethodPicker()
    }

    private fun checkKeyboardStatus() {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val enabledInputMethods = imm.enabledInputMethodList
            val isEnabled = enabledInputMethods.any { 
                it.packageName == packageName || it.packageName == "$packageName.debug"
            }

            if (!isEnabled) {
                Toast.makeText(
                    this,
                    "Vui lòng bật iOS 26 Keyboard trong cài đặt",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Vui lòng bật iOS 26 Keyboard trong cài đặt",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
