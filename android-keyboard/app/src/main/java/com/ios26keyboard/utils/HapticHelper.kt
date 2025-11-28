package com.ios26keyboard.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View

object HapticHelper {
    
    private const val LIGHT_CLICK_DURATION = 10L
    private const val MEDIUM_CLICK_DURATION = 20L
    private const val HEAVY_CLICK_DURATION = 30L
    
    fun performKeyPressHaptic(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }
    
    fun performLightHaptic(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            view.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
    }
    
    fun performLightClick(context: Context) {
        vibrate(context, LIGHT_CLICK_DURATION, VibrationEffect.EFFECT_TICK)
    }
    
    fun performMediumClick(context: Context) {
        vibrate(context, MEDIUM_CLICK_DURATION, VibrationEffect.EFFECT_CLICK)
    }
    
    fun performHeavyClick(context: Context) {
        vibrate(context, HEAVY_CLICK_DURATION, VibrationEffect.EFFECT_HEAVY_CLICK)
    }
    
    private fun vibrate(context: Context, duration: Long, effectId: Int) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}
