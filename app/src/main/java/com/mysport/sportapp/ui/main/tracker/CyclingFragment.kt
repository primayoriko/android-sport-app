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
import kotlinx.android.synthetic.main.fragment_cycling.*
import java.util.*
import kotlin.math.round

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CyclingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CyclingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val viewModel: TrackerViewModel by viewModels()

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
        setHasOptionsMenu(false)

//        return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_cycling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        btnToggleTrack.setOnClickListener {
            toggleRun()
        }

        btnFinishTrack.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }

        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        CyclingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        CyclingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        CyclingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackerUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            tvTimer.text = formattedTime
        })

        CyclingService.distanceInMeters.observe(viewLifecycleOwner, Observer {
            curDistanceInMeters = it
            val formattedDistance = TrackerUtility.getFormattedDistance(curDistanceInMeters)

            val displayedDistance = "$formattedDistance m"
            tvDistance.text = displayedDistance
        })
    }

    private fun toggleRun() {
        if(isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
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

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Cancel the Run?")
                .setMessage("Are you sure to cancel the current run and delete all its data?")
                .setIcon(R.drawable.ic_white_delete_24)
                .setPositiveButton("Yes") { _, _ ->
                    stopRun()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        dialog.show()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
//        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            btnToggleTrack.text = "Start"
            btnFinishTrack.visibility = View.VISIBLE
        } else {
            btnToggleTrack.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            btnFinishTrack.visibility = View.GONE
        }
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
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        map?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                        bounds.build(),
                        mapView.width,
                        mapView.height,
                        (mapView.height * 0.05f).toInt()
                )
        )
    }

    private fun endRunAndSaveToDb() {
//        map?.snapshot { bmp ->
//            var distanceInMeters = 0
//            for(polyline in pathPoints) {
//                distanceInMeters += TrackerUtility.calculatePolylineLength(polyline).toInt()
//            }
//            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
//            val dateTimestamp = Calendar.getInstance().timeInMillis
//            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
//            val run = Run(bmp, dateTimestamp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
//            viewModel.insertRun(run)
//            Snackbar.make(
//                    requireActivity().findViewById(R.id.rootView),
//                    "Run saved successfully",
//                    Snackbar.LENGTH_LONG
//            ).show()
//            stopRun()
//        }
        stopRun()
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
                    .color(POLYLINE_COLOR)
                    .width(POLYLINE_WIDTH)
                    .add(preLastLatLng)
                    .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), CyclingService::class.java).also {
                it.action = action
                requireContext().startService(it)
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

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment CyclingFragment.
//         */
//        // TODO: Rename and change types and number of parameters
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