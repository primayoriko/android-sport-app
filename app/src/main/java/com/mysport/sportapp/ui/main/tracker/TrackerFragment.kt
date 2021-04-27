package com.mysport.sportapp.ui.main.tracker

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.data.Constant.REQUEST_CODE_LOCATION_PERMISSION
import com.mysport.sportapp.util.TrackerUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracker.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class TrackerFragment : Fragment(),
        EasyPermissions.PermissionCallbacks, SensorEventListener {

    //    private val binding: FragmentTrackerBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private lateinit var magneticFieldSensor: Sensor

    private var floatGravity = FloatArray(3)
    private var floatGeoMagnetic = FloatArray(3)

    private var floatOrientation = FloatArray(3)
    private var floatRotationMatrix = FloatArray(9)

    companion object {
        fun newInstance() = TrackerFragment()
    }

    private lateinit var viewModel: TrackerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        buttonSelectRunning.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tracker_to_runningFragment)
        }

        buttonSelectCycling.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tracker_to_cyclingFragment)
        }

        requestPermissions()
        activateSensors()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TrackerViewModel::class.java)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            floatGravity = event.values;

        } else if(event.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD){
            floatGeoMagnetic = event.values;

        }

        SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
        SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

        imageCompass?.rotation = (-floatOrientation[0] * 180 / 3.14159).toFloat()

//        Timber.d(floatOrientation[0].toString())
//        Timber.d(floatOrientation[1].toString())
//        Timber.d(floatOrientation[2].toString())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestPermissions() {
        if(!TrackerUtility.hasLocationPermissions(requireContext())) {
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
                        "You need to accept location permissions to use this app cycling tracker.",
                        REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }

        if(!TrackerUtility.hasSensorPermissions(requireContext())) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                        this,
                        "You need to accept location permissions to use this app running tracker.",
                        REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                )

            } else {
                // TODO: Search permission needed
            }
        }
    }

    private fun activateSensors(){
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, magneticFieldSensor, 0)
        sensorManager.registerListener(this, accelerometerSensor, 0)
    }

}