package com.example.android.bleviewer

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView


class PopupMessage(context : Context) {
    var layout = LinearLayout(context)
    var tv = TextView(context)
    var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    var popUp = PopupWindow(layout)

    init {
        layout.setOrientation(LinearLayout.VERTICAL)
        layout.addView(tv, params)
    }

    fun show(message : String) {

        if(!popUp.isShowing) {

            tv.setText(message)

            popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10)
            popUp.update(50, 50, 300, 80)
        }
        else {
            popUp.dismiss()
        }
    }

}
