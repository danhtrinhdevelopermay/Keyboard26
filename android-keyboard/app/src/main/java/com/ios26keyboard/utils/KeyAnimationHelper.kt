package com.ios26keyboard.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import com.ios26keyboard.R

object KeyAnimationHelper {

    private const val PRESS_DURATION = 80L
    private const val RELEASE_DURATION = 140L
    private const val RIPPLE_DURATION = 100L
    private const val MODE_SWITCH_DURATION = 150L

    fun animateKeyPress(view: View) {
        view.animate().cancel()
        view.animate()
            .alpha(0.6f)
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(PRESS_DURATION)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun animateKeyRelease(view: View) {
        view.animate().cancel()
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(RELEASE_DURATION)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    fun animateRippleLight(view: View, isDarkMode: Boolean) {
        val overlay = View(view.context).apply {
            layoutParams = view.layoutParams
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 12f
                setColor(
                    if (isDarkMode) 
                        ContextCompat.getColor(view.context, R.color.accent_blue_light)
                    else 
                        ContextCompat.getColor(view.context, R.color.accent_blue)
                )
            }
            alpha = 0f
        }

        val fadeIn = ObjectAnimator.ofFloat(overlay, View.ALPHA, 0f, 0.3f)
        val fadeOut = ObjectAnimator.ofFloat(overlay, View.ALPHA, 0.3f, 0f)

        AnimatorSet().apply {
            playSequentially(fadeIn, fadeOut)
            duration = RIPPLE_DURATION
            start()
        }
    }

    fun animateShiftToggle(view: View, isActive: Boolean) {
        val rotation = if (isActive) 0f else 180f
        val targetRotation = if (isActive) 180f else 0f

        ObjectAnimator.ofFloat(view, View.ROTATION, rotation, targetRotation).apply {
            duration = 200L
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun animateGlobeRotation(view: View) {
        ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f).apply {
            duration = 500L
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun createSpringAnimation(view: View): AnimatorSet {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0.95f, 1.05f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0.95f, 1.05f, 1f)

        return AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 200L
        }
    }

    fun animateKeyPopup(view: View, onComplete: () -> Unit = {}) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1.2f, 1f)
        val alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
        val translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 20f, -10f, 0f)

        AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha, translationY)
            duration = 150L
            interpolator = OvershootInterpolator()
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    onComplete()
                }
            })
            start()
        }
    }

    fun animateModeSwitch(container: View, onComplete: () -> Unit) {
        container.animate().cancel()
        
        val scaleDown = ObjectAnimator.ofFloat(container, View.SCALE_Y, 1f, 0.98f)
        val alphaDown = ObjectAnimator.ofFloat(container, View.ALPHA, 1f, 0.7f)
        
        val animateOut = AnimatorSet().apply {
            playTogether(scaleDown, alphaDown)
            duration = MODE_SWITCH_DURATION / 2
            interpolator = DecelerateInterpolator()
        }
        
        animateOut.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                onComplete()
                
                val scaleUp = ObjectAnimator.ofFloat(container, View.SCALE_Y, 0.98f, 1f)
                val alphaUp = ObjectAnimator.ofFloat(container, View.ALPHA, 0.7f, 1f)
                
                AnimatorSet().apply {
                    playTogether(scaleUp, alphaUp)
                    duration = MODE_SWITCH_DURATION / 2
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        })
        
        animateOut.start()
    }
    
    fun animateLanguageSwitch(container: View, onComplete: () -> Unit) {
        container.animate().cancel()
        
        onComplete()
        
        container.scaleX = 0.95f
        container.scaleY = 0.95f
        container.alpha = 0.8f
        
        container.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(MODE_SWITCH_DURATION)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}
