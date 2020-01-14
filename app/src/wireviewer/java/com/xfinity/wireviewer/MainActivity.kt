package com.xfinity.wireviewer

import android.app.SearchManager.QUERY
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.xfinity.R
import com.xfinity.features.masterdetail.fragment.ItemDetailFragment
import com.xfinity.features.masterdetail.fragment.ItemListFragment

class MainActivity : AppCompatActivity() {
    private var itemListFragment: ItemListFragment? = null
    private var tabletSize: Boolean = false
    private var showIcon: Boolean = false
    private var toolbar: Toolbar? = null
    private var myMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            showIcon = savedInstanceState.getBoolean(SHOWICON, false)
        }

        setContentView(R.layout.activity_main)

        tabletSize = resources.getBoolean(R.bool.isTablet)

        toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        if (supportActionBar != null) { // hide
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setHomeButtonEnabled(false)
        }

        toolbar!!.setNavigationOnClickListener { v ->
            // what do you want here
            if (supportActionBar != null) { // hide
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                supportActionBar!!.setHomeButtonEnabled(false)
            }
            onBackPressed()
        }

        if (savedInstanceState != null)
            return
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar!!.title = getString(R.string.app_name)
        if (!tabletSize) {
            myMenu!!.findItem(MENU_ITEM_ITEM1).isVisible = true
        }
    }

    override fun onResume() {
        super.onResume()

        val bundle = Bundle()
        bundle.putString(QUERY, getString(R.string.search_query))

        itemListFragment = ItemListFragment()
        itemListFragment!!.retainInstance = true
        itemListFragment!!.arguments = bundle

        itemListFragment!!.showIcon = showIcon

        val fragmentManager = supportFragmentManager

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down,
                R.anim.slide_up, R.anim.slide_down)

        fragmentTransaction.replace(R.id.framelayout_left, itemListFragment)

        if (findViewById<View>(R.id.framelayout_right) != null) {
            val itemDetailFragment = ItemDetailFragment()
            itemDetailFragment.retainInstance = true
            fragmentTransaction.replace(R.id.framelayout_right, itemDetailFragment)
        }

        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putBoolean(SHOWICON, showIcon)
    }

    companion object {
        private val MENU_ITEM_ITEM1 = 1

        private val SHOWICON = "showIcon"
    }

}