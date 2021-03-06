package com.mysport.sportapp.ui.main.tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mysport.sportapp.R
import com.mysport.sportapp.util.PermissionUtility
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
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

        if(!PermissionUtility.hasSensorPermissions(requireContext()))
            PermissionUtility.requestSensorPermissions(this)

        activateSensors()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TrackerViewModel::class.java)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            floatGravity = event.values
        } else if(event.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD){
            floatGeoMagnetic = event.values
        }

        SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
        SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

        imageCompass?.rotation = (-floatOrientation[0] * 180 / 3.14159).toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show()
        } else {
            PermissionUtility.requestSensorPermissions(this)
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

    private fun activateSensors(){
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager.registerListener(this, magneticFieldSensor, 0)
        sensorManager.registerListener(this, accelerometerSensor, 0)
    }

}