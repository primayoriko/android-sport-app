package com.mysport.sportapp.ui.main.tracker

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant
import com.mysport.sportapp.data.Training
import com.mysport.sportapp.service.RunningService
import com.mysport.sportapp.ui.main.MainActivity
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cycling.btnFinishTrack
import kotlinx.android.synthetic.main.fragment_cycling.btnToggleTrack
import kotlinx.android.synthetic.main.fragment_cycling.tvTimer
import kotlinx.android.synthetic.main.fragment_running.*
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class RunningFragment : Fragment() {

    private val viewModel: TrackerViewModel by viewModels<TrackerViewModel>()

    //    private var isInitialized = false
    private var isTracking = false

    private var curStepCount = 0
    private var curTimeInMillis = 0L

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

        return inflater.inflate(R.layout.fragment_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFinishTrack.visibility = View.GONE

        btnToggleTrack.setOnClickListener {
            toggleTrack()
        }

        btnFinishTrack.setOnClickListener {
            showFinishTrackingDialog()
        }

        subscribeToObservers()
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
            Intent(requireContext(), RunningService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun subscribeToObservers() {
        RunningService.isTracking.observe(viewLifecycleOwner, Observer {
            updateButton(it)
        })

        RunningService.stepCount.observe(viewLifecycleOwner, Observer {
            curStepCount = it

            val displayedStepCount = "$curStepCount"

            tvTotalStep.text = displayedStepCount
        })

        RunningService.timeTrainInMillis.observe(viewLifecycleOwner, Observer {
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

        } else {
            btnToggleTrack.text = "Stop"
            btnFinishTrack.visibility = View.GONE

            menu?.getItem(0)?.isVisible = true
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

            sendCommandToService(Constant.ACTION_PAUSE_SERVICE)

        } else {
            sendCommandToService(Constant.ACTION_START_OR_RESUME_SERVICE)

        }
    }

    private fun endTrack() {
        val dateTimestamp = Calendar.getInstance().timeInMillis
        val trainingEntry = Training(
                Training.TrainingType.CYCLING,
                dateTimestamp,
                curTimeInMillis,
                null,
                Bitmap.createBitmap(50, 50, Bitmap.Config.ALPHA_8),
                curStepCount
        )

        Timber.d(trainingEntry.toString())

        viewModel.insertTraining(trainingEntry)
        Snackbar.make(
                requireActivity().findViewById(R.id.activity_main),
                "Training data saved successfully",
                Snackbar.LENGTH_LONG
        ).show()

        stopTrack()
    }

    private fun stopTrack() {
        sendCommandToService(Constant.ACTION_STOP_SERVICE)

        findNavController().navigate(R.id.action_runningFragment_to_navigation_tracker)
    }

//    override fun onResume() {
//        super.onResume()
//    }
//
//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//    }

}