package com.intermediate.storyapp1.ui.maps

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.intermediate.storyapp1.R
import com.intermediate.storyapp1.data.constant.Constant
import com.intermediate.storyapp1.data.model.Story
import com.intermediate.storyapp1.databinding.FragmentMapsBinding
import com.intermediate.storyapp1.ui.customView.CustomAlertDialog
import com.intermediate.storyapp1.utils.ViewModelFactory
import com.intermediate.storyapp1.data.presenter.Result

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val mapsViewModel: MapsViewModel by viewModels { factory }
    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        setupViewModel()

        val dummyLocation = LatLng(Constant.LATITUDE, Constant.LONGITUDE)
        googleMap.addMarker(
            MarkerOptions()
                .position(dummyLocation)
                .title("Marker in dummyLocation")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dummyLocation, 15f))

        setMapStyle(googleMap)
        getStoryWithLocation(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getStoryWithLocation(googleMap: GoogleMap) {
        mapsViewModel.getStories().observe(this) { result ->
            if (result != null) {
                when(result) {
                    is com.intermediate.storyapp1.data.presenter.Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        errorHandler()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        showMarker(result.data.listStory, googleMap)
                    }
                }
            }
        }
    }

    private fun showMarker(listStory: List<Story>, googleMap: GoogleMap) {
        listStory.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.createdAt)
                    .snippet(StringBuilder("created: ")
                        .append(story.createdAt.subSequence(11, 16).toString())
                        .toString()
                    )
            )
            boundsBuilder.include(latLng)
        }
    }

    private fun errorHandler() {
        CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error_form).show()
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(binding.root.context, R.raw.map_style))
            if (!success) {
                CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error_form).show()
            }
        } catch (exception: Resources.NotFoundException) {
            CustomAlertDialog(binding.root.context, R.string.error_message, R.drawable.error_form).show()
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMap.visibility = View.VISIBLE
            binding.root.visibility = View.INVISIBLE
        } else {
            binding.progressBarMap.visibility = View.INVISIBLE
            binding.root.visibility = View.VISIBLE
        }
    }

}