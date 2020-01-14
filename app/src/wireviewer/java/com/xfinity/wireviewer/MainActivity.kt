package com.xfinity.wireviewer

import android.app.SearchManager.QUERY
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.xfinity.R
import com.xfinity.features.masterdetail.CharacterViewModel
import com.xfinity.features.masterdetail.fragment.ItemDetailFragment
import com.xfinity.features.masterdetail.fragment.ItemDetailFragment.Companion.ICON
import com.xfinity.features.masterdetail.fragment.ItemDetailFragment.Companion.ITEM
import com.xfinity.features.masterdetail.fragment.ItemListFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private var itemListFragment: ItemListFragment? = null
    private var tabletSize: Boolean = false
    private var showIcon: Boolean = false
    private var toolbar: Toolbar? = null
    private lateinit var myMenu: Menu

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

        toolbar?.setNavigationOnClickListener {
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

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar!!.title = getString(R.string.app_name)
        if (!tabletSize) {
            myMenu?.findItem(MENU_ITEM_ITEM1)?.isVisible = true
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CharacterViewModel) {/* Do something */
        supportActionBar?.let {
            if (!tabletSize) {
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeButtonEnabled(true)
                toolbar?.title = event.characterName.value
                myMenu?.findItem(MENU_ITEM_ITEM1)?.setVisible(false)
            } else {
                it.setDisplayHomeAsUpEnabled(false)
                it.setHomeButtonEnabled(false)
            }
        }

        val fragmentManager = supportFragmentManager

        val fragmentTransaction = fragmentManager.beginTransaction()
        if (!tabletSize) {
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit,
                    R.anim.pop_enter, R.anim.pop_exit)
        }

        var containerViewId = R.id.framelayout_left

        if (findViewById<View>(R.id.framelayout_right) != null)
            containerViewId = R.id.framelayout_right

        val bundle = Bundle()
        event.characterName.value?.let { name ->
            bundle.putString(ITEM, name)
        }
        event.characterImage.value?.let { url ->
            bundle.putString(ICON, url)
        }

        val itemDetailFragment = ItemDetailFragment()
        itemDetailFragment.retainInstance = true
        itemDetailFragment.arguments = bundle
        fragmentTransaction.replace(containerViewId, itemDetailFragment)

        if (findViewById<View>(R.id.framelayout_right) == null)
            fragmentTransaction.addToBackStack(null)

        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putBoolean(SHOWICON, showIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        myMenu = menu
        if (!tabletSize) {
            //itemListFragment.setIconVisibility(showIcon)
            if (showIcon) {
                menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, getString(R.string.show_text))
            } else {
                menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, getString(R.string.show_images))
            }

            val inflater = menuInflater
            inflater.inflate(R.menu.menu, menu)

            /*searchMenuItem = menu.findItem(R.id.action_search)
            if (itemListFragment != null) {
                itemListFragment.setupSearchView(searchMenuItem)
            }*/
        }
        return true
    }

    companion object {
        private val MENU_ITEM_ITEM1 = 1
        private val SHOWICON = "showIcon"
    }

}