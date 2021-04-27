package com.mysport.sportapp.ui.main.tracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant.REQUEST_CODE_LOCATION_PERMISSION
import com.mysport.sportapp.databinding.FragmentTrackerBinding
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracker.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class TrackerFragment : Fragment(), EasyPermissions.PermissionCallbacks {
//    private val binding: FragmentTrackerBinding

    companion object {
        fun newInstance() = TrackerFragment()
    }

    private lateinit var viewModel: TrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val spinner: Spinner? =  view.findViewById(R.id.dropdownTracker)
//
//        val values = resources.getStringArray(R.array.track_type)
//        val adapter = ArrayAdapter(
//            this.requireActivity(),
//            android.R.layout.simple_spinner_item,
//            values
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//
//        spinner?.adapter = adapter
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
////                Log.v("item", parent.getItemAtPosition(position) as String)
//                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }

        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val runningButton: Button? =  view.findViewById(R.id.buttonSelectRunning)
//        val cyclingButton: Button? =  view.findViewById(R.id.buttonSelectCycling)
//
//        runningButton?.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_tracker_to_runningFragment)
//        }
//
//        cyclingButton?.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_tracker_to_cyclingFragment)
//        }

        buttonSelectRunning.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tracker_to_runningFragment)
        }

        buttonSelectCycling.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tracker_to_cyclingFragment)
        }

        requestPermissions()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TrackerViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun requestPermissions() {
        if(TrackerUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

//    override fun onClick(view: View) {
//        var fragment: Fragment? = null
//        Log.v(view.id.toString(), "clicked")
//        when (view.id) {
//            R.id.buttonSelectCycling -> {
//                fragment = CyclingFragment()
//                replaceFragment(fragment)
//            }
//            R.id.buttonSelectRunning -> {
//                fragment = RunningFragment()
//                replaceFragment(fragment)
//            }
//        }
//    }
//
//    private fun replaceFragment(someFragment: Fragment?) {
//        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//        if (someFragment != null) {
//            transaction.replace(R.id.fragment_container_tracker, someFragment)
//        }
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

}