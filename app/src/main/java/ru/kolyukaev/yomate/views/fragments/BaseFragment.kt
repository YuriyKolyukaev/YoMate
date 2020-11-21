package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.views.activities.MainActivity

abstract class BaseFragment: MvpAppCompatFragment() {

    abstract val toolbarName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("${this.javaClass.name} comes on the screen.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is MainActivity) {
            (activity as MainActivity).setToolbarName(toolbarName)
        }
    }

}