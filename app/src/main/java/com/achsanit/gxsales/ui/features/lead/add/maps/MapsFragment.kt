package com.achsanit.gxsales.ui.features.lead.add.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.achsanit.gxsales.R
import com.achsanit.gxsales.databinding.FragmentMapsBinding
import com.achsanit.gxsales.ui.features.main.MainActivity
import com.achsanit.gxsales.utils.makeGone
import com.achsanit.gxsales.utils.makeVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null

    private val locationSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isLocationEnabled()) {
                getCurrentLocation()
            } else {
                // Handle if location services are still not enabled
                findNavController().navigateUp()
            }
        }

    private lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        map = googleMap
        map.uiSettings.apply {
            isCompassEnabled = false // To hide google maps compass icon
            isMapToolbarEnabled = false // To hide google maps compass icon
            isZoomControlsEnabled = true // show zoom in and out control
            isMyLocationButtonEnabled = true
        }

        checkLocationService()

        map.setOnMapClickListener {
            map.clear()

            moveCamera(it, null, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSelectLocation.setOnClickListener {
                val navPopBackStack = findNavController()
                navPopBackStack.previousBackStackEntry?.savedStateHandle?.set(
                    SELECTED_LOCATION_KEY,
                    currentLocation
                )
                navPopBackStack.popBackStack()
            }

            buttonReset.setOnClickListener {
                map.clear()
                currentLocation = null
                btnSelectLocation.makeGone()
            }
        }
    }

    private fun moveCamera(
        latLng: LatLng,
        zoom: Float?,
        isSelectedLocation: Boolean? = false
    ) {
        binding.pbMap.makeGone()

        isSelectedLocation?.let { isSelectLocation ->
            if (isSelectLocation) {
                currentLocation = latLng

                map.addMarker(
                    MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map))
                )

                binding.btnSelectLocation.makeVisible()
            }
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: 18.0f))
    }

    private fun checkLocationService() {
        if (isLocationEnabled()) {
            getCurrentLocation()
        } else {
            // If location services are not enabled, prompt the user to enable them
            context?.let {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                locationSettingsLauncher.launch(intent)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return (requireActivity() as MainActivity).requestLocationPermission()
        }

        binding.pbMap.makeVisible()
        // TODO: Improve get current location using network, its more precision
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                it?.let {
                    moveCamera(LatLng(it.latitude, it.longitude), null, true)
                } ?: moveCamera(
                    LatLng(-6.200000, 106.816666),
                    12.0f,
                    false
                )
            }
            .addOnFailureListener {
                binding.pbMap.makeGone()
                Snackbar.make(
                    binding.root,
                    "Failed to get current location, please select manual on map",
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.ok)) {}
                    .setActionTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        const val SELECTED_LOCATION_KEY = "selected.location.key"
    }
}