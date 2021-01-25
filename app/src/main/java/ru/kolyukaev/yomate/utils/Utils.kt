package ru.kolyukaev.yomate.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun Any.log(text: Any?) {
    Log.i("KYUS", text.toString())
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
//heightDisplay 2118  cl_transparent.height 404   getStatusBarHeight 63   pxFromDp(view!!.context, 8) 21
//heightDisplay 2160  cl_transparent.height 423   getStatusBarHeight 145   pxFromDp(view!!.context, 8) 22

fun dpFromPx(context: Context, px: Float): Float {
    return px / context.getResources().getDisplayMetrics().density
}

fun pxFromDp(context: Context, dp: Int): Int {
    return (dp * context.getResources().getDisplayMetrics().density).toInt()
}

fun View.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(
            left ?: marginLeft,
            top ?: marginTop,
            right ?: marginRight,
            bottom ?: marginBottom
        )
        this.requestLayout()
    }
}
