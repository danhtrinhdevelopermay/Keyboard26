package com.ios26keyboard.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.ios26keyboard.R

class KeyPopupHelper(private val context: Context) {
    
    private var popupWindow: PopupWindow? = null
    private var popupView: TextView? = null
    
    fun show(anchorView: View, keyText: String) {
        dismiss()
        
        val inflater = LayoutInflater.from(context)
        popupView = inflater.inflate(R.layout.key_popup_view, null) as TextView
        popupView?.text = keyText
        
        popupWindow = PopupWindow(
            popupView,
            (60 * context.resources.displayMetrics.density).toInt(),
            (70 * context.resources.displayMetrics.density).toInt(),
            false
        ).apply {
            isClippingEnabled = false
            elevation = 8f
            
            val location = IntArray(2)
            anchorView.getLocationInWindow(location)
            
            val xOffset = (anchorView.width - width) / 2
            val yOffset = -height - (5 * context.resources.displayMetrics.density).toInt()
            
            showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0] + xOffset, location[1] + yOffset)
        }
        
        KeyAnimationHelper.animateKeyPopup(popupView!!)
    }
    
    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
        popupView = null
    }
    
    fun isShowing(): Boolean {
        return popupWindow?.isShowing == true
    }
}
