package ru.kolyukaev.yoweather.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
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

fun doDelayed(time: Long, block: () -> Unit) {
    Handler().postDelayed({
        try {
            block()
        } catch (e: Exception) {
            Log.e("KYUS", "doDelayed", e)
        }
    }, time)
}

