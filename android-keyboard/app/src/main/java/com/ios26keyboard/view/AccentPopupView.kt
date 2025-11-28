package com.ios26keyboard.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ios26keyboard.R
import com.ios26keyboard.utils.HapticHelper

class AccentPopupView(private val context: Context) {
    
    private var popupWindow: PopupWindow? = null
    private var onAccentSelected: ((String) -> Unit)? = null
    private var selectedIndex = -1
    private var accentViews = mutableListOf<TextView>()
    
    private val accentMap = mapOf(
        'a' to listOf("à", "á", "ả", "ã", "ạ", "ă", "ằ", "ắ", "ẳ", "ẵ", "ặ", "â", "ầ", "ấ", "ẩ", "ẫ", "ậ"),
        'e' to listOf("è", "é", "ẻ", "ẽ", "ẹ", "ê", "ề", "ế", "ể", "ễ", "ệ"),
        'i' to listOf("ì", "í", "ỉ", "ĩ", "ị"),
        'o' to listOf("ò", "ó", "ỏ", "õ", "ọ", "ô", "ồ", "ố", "ổ", "ỗ", "ộ", "ơ", "ờ", "ớ", "ở", "ỡ", "ợ"),
        'u' to listOf("ù", "ú", "ủ", "ũ", "ụ", "ư", "ừ", "ứ", "ử", "ữ", "ự"),
        'y' to listOf("ỳ", "ý", "ỷ", "ỹ", "ỵ"),
        'd' to listOf("đ")
    )
    
    fun hasAccents(char: Char): Boolean {
        return accentMap.containsKey(char.lowercaseChar())
    }
    
    fun getAccents(char: Char): List<String> {
        val isUpper = char.isUpperCase()
        val accents = accentMap[char.lowercaseChar()] ?: return emptyList()
        return if (isUpper) accents.map { it.uppercase() } else accents
    }
    
    fun show(anchorView: View, char: Char, onSelected: (String) -> Unit) {
        dismiss()
        
        val accents = getAccents(char)
        if (accents.isEmpty()) return
        
        onAccentSelected = onSelected
        selectedIndex = -1
        accentViews.clear()
        
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            background = createPopupBackground()
            elevation = 8f
            setPadding(8, 8, 8, 8)
        }
        
        accents.forEachIndexed { index, accent ->
            val textView = createAccentTextView(accent, index)
            accentViews.add(textView)
            container.addView(textView)
        }
        
        popupWindow = PopupWindow(
            container,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        ).apply {
            isOutsideTouchable = false
            isTouchable = false
            animationStyle = android.R.style.Animation_Dialog
        }
        
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        
        container.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = container.measuredWidth
        val xOffset = location[0] + (anchorView.width / 2) - (popupWidth / 2)
        val yOffset = location[1] - container.measuredHeight - 20
        
        popupWindow?.showAtLocation(anchorView, Gravity.NO_GRAVITY, xOffset, yOffset)
    }
    
    fun updateSelection(anchorView: View, touchX: Float) {
        if (accentViews.isEmpty() || popupWindow == null) return
        
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        
        val popupLocation = IntArray(2)
        (popupWindow?.contentView as? ViewGroup)?.getLocationOnScreen(popupLocation)
        
        val relativeX = touchX + location[0] - popupLocation[0]
        
        var newSelectedIndex = -1
        var currentX = 8f
        
        accentViews.forEachIndexed { index, view ->
            val viewWidth = view.width.toFloat()
            if (relativeX >= currentX && relativeX < currentX + viewWidth) {
                newSelectedIndex = index
            }
            currentX += viewWidth
        }
        
        if (newSelectedIndex != selectedIndex) {
            selectedIndex = newSelectedIndex
            updateHighlight()
            if (selectedIndex >= 0) {
                HapticHelper.performKeyPressHaptic(anchorView)
            }
        }
    }
    
    private fun updateHighlight() {
        accentViews.forEachIndexed { index, view ->
            if (index == selectedIndex) {
                view.background = createHighlightBackground()
                view.setTextColor(Color.WHITE)
            } else {
                view.background = null
                view.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }
    
    fun confirmSelection(): Boolean {
        if (selectedIndex >= 0 && selectedIndex < accentViews.size) {
            val selectedAccent = accentViews[selectedIndex].text.toString()
            onAccentSelected?.invoke(selectedAccent)
            dismiss()
            return true
        }
        dismiss()
        return false
    }
    
    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
        accentViews.clear()
        selectedIndex = -1
    }
    
    fun isShowing(): Boolean = popupWindow?.isShowing == true
    
    private fun createAccentTextView(accent: String, index: Int): TextView {
        return TextView(context).apply {
            text = accent
            textSize = 20f
            setTextColor(ContextCompat.getColor(context, R.color.white))
            gravity = Gravity.CENTER
            setPadding(24, 16, 24, 16)
            minWidth = 44
        }
    }
    
    private fun createPopupBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(Color.parseColor("#2C2C2E"))
            cornerRadius = 16f
        }
    }
    
    private fun createHighlightBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.accent_blue))
            cornerRadius = 12f
        }
    }
}
