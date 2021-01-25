package ru.kolyukaev.yomate.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.views.activities.MainActivity

abstract class BaseFragment: MvpAppCompatFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("${this.javaClass.name} comes on the screen.")
    }

    // скрыаатие клавиатуры
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}