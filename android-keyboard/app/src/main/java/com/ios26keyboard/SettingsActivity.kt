package com.ios26keyboard

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupSettings()
    }

    private fun setupSettings() {
        val prefs = getSharedPreferences("keyboard_prefs", MODE_PRIVATE)

        findViewById<Switch>(R.id.switch_haptic)?.apply {
            isChecked = prefs.getBoolean("haptic_enabled", true)
            setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("haptic_enabled", isChecked).apply()
            }
        }

        findViewById<Switch>(R.id.switch_sound)?.apply {
            isChecked = prefs.getBoolean("sound_enabled", false)
            setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("sound_enabled", isChecked).apply()
            }
        }

        findViewById<Switch>(R.id.switch_autocorrect)?.apply {
            isChecked = prefs.getBoolean("autocorrect_enabled", true)
            setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("autocorrect_enabled", isChecked).apply()
            }
        }

        findViewById<Switch>(R.id.switch_suggestions)?.apply {
            isChecked = prefs.getBoolean("suggestions_enabled", true)
            setOnCheckedChangeListener { _, isChecked ->
                prefs.edit().putBoolean("suggestions_enabled", isChecked).apply()
            }
        }
    }
}
