package com.achsanit.gxsales.ui.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.transition.R.anim
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.ActivityMainBinding
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavController()
    }

    // this function for setup bottom navigation with navigation component
    private fun setUpNavController() {
        val navView: BottomNavigationView = binding.bottomNav // get bottom nav
        val navController = findNavController(R.id.nav_host_main_fragment) // set nav controller

        navView.setupWithNavController(navController) // setup bottom nav with nav controller
        navView.menu.getItem(2).isEnabled = false // disable center item in bottom nav (space for FAB)

        val navOptions = NavOptions.Builder()
            .setEnterAnim(anim.abc_slide_in_bottom) // Specify the animation resource for entering the destination
            .setPopExitAnim(anim.abc_slide_out_bottom) // Specify the animation resource for exiting the destination when popped from back stack
            .build()

        binding.fabLead.setOnClickListener {
            // navigate to add lead fragment
            navController.navigate(R.id.createUpdateLeadFragment, null, navOptions)
        }

        // listener when destination nav controller changed
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home -> {
                    setBottomNav(true) // show bottom nav
                }
                R.id.nav_account -> {
                    setBottomNav(true)
                }
                R.id.nav_shop -> {
                    setBottomNav(true)
                }
                R.id.nav_prospect -> {
                    setBottomNav(true)
                }
                else -> {
                    setBottomNav(false) // hide bottom nav
                }
            }
        }
    }

    // this function using for show and hide the bottom navigation
    private fun setBottomNav(show: Boolean) {
        with(binding) {
            if (show) {
                bottomNav.makeVisible()
                fabLead.makeVisible()
            } else {
                bottomNav.makeGone()
                fabLead.makeGone()
            }
        }
    }
}