package ru.kolyukaev.yomate.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.kolyukaev.yomate.*
import ru.kolyukaev.yomate.utils.gone
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.utils.visible
import ru.kolyukaev.yomate.views.fragments.CityFragment
import ru.kolyukaev.yomate.views.fragments.MainFragment

class MainActivity : MvpAppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    var onToolbarTextChanged: ((text: String) -> Unit)? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate (MainActivity)")

        fragmentManager = supportFragmentManager

        setSupportActionBar(toolbar)

        val fragment1 = MainFragment()

        commitFragmentTransaction(fragment1, false)
        log("commitFragmentTransaction fragment1")

        setToolbarTextChangedListener()
    }


    override fun onBackPressed() {

        var count = fragmentManager.backStackEntryCount

        if (count > 0) {
            fragmentManager.popBackStack()
        } else {
            Toast.makeText(this, "Exit",
                Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.city_info_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val cityFragment = CityFragment()
            commitFragmentTransaction(cityFragment, true)
            log("commitFragmentTransaction fragment2")
        }
        return true
    }


    fun commitFragmentTransaction(fragment: Fragment, isAddToBackStack: Boolean) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (isAddToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun setToolbarName(name: String) {
        tv_toolbar.text = name
    }

    fun goneToolbar() {
        et_toolbar_search.gone()
    }

    fun visibleToolbar() {
        et_toolbar_search.visible()
    }

    fun enlargeToolbar() {
        tv_toolbar.textSize = 25F
    }

    fun decrease() {
        tv_toolbar.textSize = 10F
    }

    @SuppressLint("SetTextI18n")
    fun visibleCityandCountry(country: String?, name: String?) {
        tv_change_city.visible()
        tv_change_city.text = name

        tv_country.visible()
        tv_country.text = "$country:"
    }

    fun goneCityandCountry() {
        tv_change_city.gone()
        tv_country.gone()
    }

    fun clearEditText() {
        et_toolbar_search.setText("")
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