package ru.kolyukaev.yomate.views.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.kolyukaev.yomate.*
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.views.fragments.MainFragment

class MainActivity : MvpAppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    //    (при использовании тулбара в активити)
    //    var onToolbarTextChanged: ((text: String) -> Unit)? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate (MainActivity)")

        fragmentManager = supportFragmentManager

        val mainFragment = MainFragment()

        commitFragmentTransaction(mainFragment, false)
        log("commitFragmentTransaction fragment1")
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)

//        setToolbarTextChangedListener()
    }

    override fun onBackPressed() {

        val count = fragmentManager.backStackEntryCount

        if (count > 0) {
            fragmentManager.popBackStack()
        } else {
            Toast.makeText(
                this, "Exit",
                Toast.LENGTH_SHORT
            ).show()
            super.onBackPressed()
        }
    }


    fun commitFragmentTransaction(fragment: Fragment, isAddToBackStack: Boolean) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (isAddToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }


    // скрывать клавиатуру
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

//    слушатель
//    private fun setToolbarTextChangedListener() {
//        et_toolbar_search.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun afterTextChanged(s: Editable?) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                onToolbarTextChanged?.invoke(s.toString())
//            }
//        })
//    }

//    (при использовании тулбара в активити) Привязка кнопки action_searck к тулбару
//    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
//        menuInflater.inflate(R.menu.city_info_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.action_search) {
//            val cityFragment = CityFragment()
//            commitFragmentTransaction(cityFragment, true)
//            log("commitFragmentTransaction fragment2")
//        }
//        return true
//    }

}