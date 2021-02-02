package ru.kolyukaev.yoweather.views.activities
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.kolyukaev.yoweather.*
import ru.kolyukaev.yoweather.views.fragments.CityFragment
import ru.kolyukaev.yoweather.views.fragments.MainFragment

class MainActivity : MvpAppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    var cityIdState = 0
    var preferences: SharedPreferences? = null

    //    (при использовании тулбара в активити)
    //    var onToolbarTextChanged: ((text: String) -> Unit)? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        fragmentManager = supportFragmentManager
        preferences = getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        cityIdState = preferences?.getInt("id", 0)!!

        setFirstFragment()
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)
    }

    fun setFirstFragment() {
        val mainFragment = MainFragment()
        val cityFragment = CityFragment()

        if (cityIdState == 0 ) {
            commitFragmentTransaction(cityFragment)
        } else {
            commitFragmentTransaction(mainFragment)
        }
    }

    override fun onBackPressed() {
        val currentFragment = fragmentManager.findFragmentById(R.id.container)

        if (currentFragment is MainFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun onClickBack(view: View) {
        onBackPressed()
    }

    fun commitFragmentTransaction(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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

