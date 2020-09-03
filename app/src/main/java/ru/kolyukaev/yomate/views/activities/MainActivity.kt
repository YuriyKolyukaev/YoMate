package ru.kolyukaev.yomate.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.views.adapter.CityAdapter
import ru.kolyukaev.yomate.views.fragments.CityFragment
import ru.kolyukaev.yomate.views.fragments.MainFragment

class MainActivity : MvpAppCompatActivity() {

    private var fragmentTransaction: FragmentManager? = null
    private lateinit var mAdapter: CityAdapter
    private lateinit var mRecyclerView: RecyclerView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("onCreate (MainActivity)")


        mRecyclerView = DELETE_RECYCLER

        fragmentTransaction = supportFragmentManager

        setSupportActionBar(toolbar)

        val fragment1 = MainFragment()

        mAdapter = CityAdapter()
        
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)

        commitFragmentTransaction(fragment1, "YoMate")
        log("commitFragmentTransaction fragment1")
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
        return super.onOptionsItemSelected(item)
    }

    fun commitFragmentTransaction(fragment: Fragment, title: String) {
        fragmentTransaction!!.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
        supportActionBar?.title = title
    }
}