package com.mysport.sportapp.data

import android.graphics.Color

object Constant {
    const val DATABASE_NAME = "sport_db"

    const val NEWS_BASE_URL = "https://newsapi.org/"
    const val NEWS_BASE_URL_PATH = "v2/top-headlines/"

    const val SHARED_PREFERENCES_NAME = "sharedPref"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val TRACKER_UPDATE_INTERVAL = 50L

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

}