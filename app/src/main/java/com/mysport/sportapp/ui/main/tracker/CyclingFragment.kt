package com.mysport.sportapp.ui.main.tracker

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant.ACTION_PAUSE_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_START_OR_RESUME_SERVICE
import com.mysport.sportapp.data.Constant.ACTION_STOP_SERVICE
import com.mysport.sportapp.data.Constant.MAP_ZOOM
import com.mysport.sportapp.data.Constant.POLYLINE_COLOR
import com.mysport.sportapp.data.Constant.POLYLINE_WIDTH
import com.mysport.sportapp.service.CyclingService
import com.mysport.sportapp.util.TrackerUtility
import com.mysport.sportapp.data.Polyline
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cycling.*
import timber.log.Timber
import java.util.*
import kotlin.math.round

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CyclingFragment : Fragment() {

    private val viewModel: TrackerViewModel by viewModels<TrackerViewModel>()

//    private var isInitialized = false
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var curTimeInMillis = 0L
    private var curDistanceInMeters = 0F

    private var menu: Menu? = null

//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val act = requireActivity() as MainActivity

        act.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        act.supportActionBar?.setHomeButtonEnabled(false)
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_cycling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        btnFinishTrack.visibility = View.GONE

        btnToggleTrack.setOnClickListener {
            toggleTrack()
        }

        btnFinishTrack.setOnClickListener {
//            endTrack()
            showFinishTrackingDialog()
        }

        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        subscribeToObservers()
    }


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracker_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(curTimeInMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
            menu.getItem(0)?.isVisible = true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.miCancelTrack -> {
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), CyclingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun subscribeToObservers() {
        CyclingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateButton(it)
        })

        CyclingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        CyclingService.distanceInMeters.observe(viewLifecycleOwner, Observer {
            curDistanceInMeters = it
            val formattedDistance = TrackerUtility.getFormattedDistance(curDistanceInMeters)
            val displayedDistance = "$formattedDistance m"

            tvDistance.text = displayedDistance
        })

        CyclingService.timeTrainInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it

            val formattedTime = TrackerUtility.getFormattedStopWatchTime(curTimeInMillis, true)

            tvTimer.text = formattedTime
        })
    }

    private fun updateButton(isTracking: Boolean) {
        this.isTracking = isTracking

        if(!isTracking) {
            btnToggleTrack.text = "Start"
            btnFinishTrack.visibility = View.VISIBLE
//            Timber.d("2 $isInitialized $isTracking sadsa")

        } else {
            btnToggleTrack.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            btnFinishTrack.visibility = View.GONE
//            Timber.d("3 $isInitialized $isTracking sadsa")

        }
    }

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Cancel")
                .setMessage("Are you sure to cancel the current tracking?")
                .setIcon(R.drawable.ic_white_delete_24)
                .setPositiveButton("Yes") { _, _ ->
                    stopTrack()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        dialog.show()
    }

    private fun showFinishTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("End")
                .setMessage("Are you sure to end the current tracking?")
                .setIcon(R.drawable.ic_white_delete_24)
                .setPositiveButton("Yes") { _, _ ->
                    endTrack()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        dialog.show()
    }

    private fun toggleTrack() {
        if(isTracking) {
            menu?.getItem(0)?.isVisible = true

            sendCommandToService(ACTION_PAUSE_SERVICE)

        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)

        }
    }

    private fun endTrack() {
        zoomToSeeWholeTrack()

        map?.snapshot { bmp ->
            val dateTimestamp = Calendar.getInstance().timeInMillis
            var distanceInMeters = 0F
            for(polyline in pathPoints) {
                distanceInMeters += TrackerUtility.calculatePolylineLength(polyline)
            }
            val trainingEntry = Training(
                    Training.TrainingType.CYCLING,
                    dateTimestamp,
                    curTimeInMillis,
                    distanceInMeters,
                    bmp,
                    null
            )

            Timber.d(trainingEntry.toString())

            viewModel.insertTraining(trainingEntry)
            Snackbar.make(
                    requireActivity().findViewById(R.id.activity_main),
                    "Training data saved successfully",
                    Snackbar.LENGTH_LONG
            ).show()
        }

        stopTrack()
    }

    private fun stopTrack() {
        sendCommandToService(ACTION_STOP_SERVICE)

        findNavController().navigate(R.id.action_cyclingFragment_to_navigation_tracker)
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            pathPoints.last().last(),
                            MAP_ZOOM
                    )
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()

        if(pathPoints.isEmpty()) return
        var cnt = 0

        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
                cnt++
            }
        }

        if(cnt == 0) return

        map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        mapView.width,
                        mapView.height,
                        (mapView.height * 0.05f).toInt()
                )
        )
    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                    .width(POLYLINE_WIDTH)
                    .color(POLYLINE_COLOR)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment CyclingFragment.
//         */
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                CyclingFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
}