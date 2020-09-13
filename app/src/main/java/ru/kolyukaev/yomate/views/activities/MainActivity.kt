package ru.kolyukaev.yomate.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.gone
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.views.fragments.CityFragment
import ru.kolyukaev.yomate.views.fragments.MainFragment
import ru.kolyukaev.yomate.visible

class MainActivity : MvpAppCompatActivity() {

    private var fragmentManager: FragmentManager? = null
    var onToolbarTextChanged: ((text: String) -> Unit)? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate (MainActivity)")

        fragmentManager = supportFragmentManager

        setSupportActionBar(toolbar)

        val fragment1 = MainFragment()

        commitFragmentTransaction(fragment1, "YoMate")

        log("commitFragmentTransaction fragment1")
        setToolbarTextChangedListener()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.city_info_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val fragment3 = CityFragment()
            commitFragmentTransaction(fragment3, "Cities")
            log("commitFragmentTransaction fragment3")
        }
        return true
    }

    fun commitFragmentTransaction(fragment: Fragment, title: String) {
        fragmentManager!!.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
        tv_toolbar.text = title
    }

    fun goneToolbar() {
        et_toolbar_search.gone()
    }

    fun visibleToolbar(){
        et_toolbar_search.visible()
    }

    private fun setToolbarTextChangedListener() {
        et_toolbar_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onToolbarTextChanged?.invoke(s.toString())
            }
        })
    }
}