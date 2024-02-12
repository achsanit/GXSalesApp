package com.achsanit.gxsales.ui.features.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build
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
import com.vmadalin.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavController()

        if (!hasAllPermission()) requestAllPermission()
    }

    private fun hasAllPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                this,
                CAMERA,
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION,
                READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.hasPermissions(
                this,
                CAMERA,
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION,
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun requestAllPermission() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.message_rationale_all_permission),
                PERMISSION_ALL_REQUEST_CODE,
                CAMERA,
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION,
                READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.message_rationale_all_permission),
                PERMISSION_ALL_REQUEST_CODE,
                CAMERA,
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }

    }

    fun requestCameraStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.message_rationale_all_permission),
                PERMISSION_CAMERA_REQUEST_CODE,
                CAMERA,
                READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.message_rationale_all_permission),
                PERMISSION_CAMERA_REQUEST_CODE,
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun hasCameraStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                this,
                CAMERA,
                READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.hasPermissions(
                this,
                CAMERA,
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.message_rationale_all_permission),
            PERMISSION_LOCATION_REQUEST_CODE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION
        )
    }

    fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            CAMERA,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION
        )
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

    companion object {
        const val PERMISSION_ALL_REQUEST_CODE = 101
        const val PERMISSION_CAMERA_REQUEST_CODE = 102
        const val PERMISSION_LOCATION_REQUEST_CODE = 102
    }
}