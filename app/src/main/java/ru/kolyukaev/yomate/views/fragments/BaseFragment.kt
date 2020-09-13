package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.kolyukaev.yomate.log

abstract class BaseFragment: MvpAppCompatFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("${this.javaClass.name} comes on the screen.")
    }

}