package com.defalt.cryptocoinex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.defalt.cryptocoinex.databinding.ActivityMainBinding
import com.defalt.cryptocoinex.fragment.HomeFragment
import com.defalt.cryptocoinex.fragment.MarketFragment
import com.defalt.cryptocoinex.fragment.WatchlistFragment
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

//    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val navController = navHostFragment!!.findNavController()

//        val popupMenu = PopupMenu(this,null)
//        popupMenu.inflate(R.menu.bottom_nav_menu)
//        binding.bottomBar.setupWithNavController(popupMenu.menu, navController)

        val navBar = findViewById<NavigationBarView>(R.id.bottomBar)
        navBar.setOnItemSelectedListener { id ->
            when(id.itemId){
                R.id.homeFragment -> {
                    val fragmentHome = HomeFragment()
                    replaceCurrentFragment(fragmentHome)
                    true
                }R.id.marketFragment -> {
                    val fragmentMarket = MarketFragment()
                    replaceCurrentFragment(fragmentMarket)
                    true
                }R.id.watchListFragment -> {
                    val fragmentWatchlist = WatchlistFragment()
                    replaceCurrentFragment(fragmentWatchlist)
                    true
                }else ->{
                    false
                }
            }
        }


    }
    private fun replaceCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
}