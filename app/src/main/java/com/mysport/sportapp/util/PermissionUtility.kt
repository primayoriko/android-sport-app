package com.mysport.sportapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.mysport.sportapp.data.Constant
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtility {

    fun hasSensorPermissions(context: Context) =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACTIVITY_RECOGNITION
                )
            } else {
                true
            }

    fun hasLocationPermissions(context: Context) =
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } else {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }

    fun requestSensorPermissions(activity: Activity) =
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                        activity,
                        "You need to accept location permissions to use this app.",
                        Constant.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                EasyPermissions.requestPermissions(
                        activity,
                        "You need to accept location permissions to use this app cycling tracker.",
                        Constant.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }

    fun requestSensorPermissions(fragment: Fragment) =
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                        fragment,
                        "You need to accept location permissions to use this app.",
                        Constant.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                EasyPermissions.requestPermissions(
                        fragment,
                        "You need to accept location permissions to use this app cycling tracker.",
                        Constant.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }

    fun requestLocationPermissions(fragment: Fragment) =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                        fragment,
                        "You need to accept location permissions to use this app running tracker.",
                        Constant.REQUEST_CODE_LOCATION_PERMISSION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                )
            } else {
                // TODO: Search permission needed
            }

}