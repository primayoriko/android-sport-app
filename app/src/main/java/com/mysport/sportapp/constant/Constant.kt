package com.mysport.sportapp.constant

import android.graphics.Color

object Constant {

    const val DATABASE_NAME = "sport_db"

    const val NEWS_API_BASE_URL = "https://newsapi.org/"
    const val NEWS_API_BASE_URL_PATH = "v2/top-headlines/"

    const val NEWS_API_CATEGORY_QUERY = "sports"
    const val NEWS_API_COUNTRY_QUERY = "id"
    const val NEWS_API_API_KEY_QUERY = "44fa6725eac7435d89e9c59a8fec8688"

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

    const val BASE_NOTIFICATION_CHANNEL_ID = "base"
    const val BASE_NOTIFICATION_CHANNEL_NAME = "Base"
    const val BASE_NOTIFICATION_CHANNEL_TITLE = "MySport App"
    const val BASE_NOTIFICATION_ID = 0

    const val RUNNING_NOTIFICATION_CHANNEL_ID = "running_tracker"
    const val RUNNING_NOTIFICATION_CHANNEL_NAME = "RunningTracker"
    const val RUNNING_NOTIFICATION_CHANNEL_TITLE = "MySport - Running Tracker"
    const val RUNNING_NOTIFICATION_ID = 2

    const val CYCLING_NOTIFICATION_CHANNEL_ID = "cycling_tracker"
    const val CYCLING_NOTIFICATION_CHANNEL_NAME = "CyclingTracker"
    const val CYCLING_NOTIFICATION_CHANNEL_TITLE = "MySport - Cycling Tracker"
    const val CYCLING_NOTIFICATION_ID = 1

    const val SCHEDULER_NOTIFICATION_CHANNEL_ID = "scheduler"
    const val SCHEDULER_NOTIFICATION_CHANNEL_NAME = "Scheduler"
    const val SCHEDULER_NOTIFICATION_CHANNEL_TITLE = "MySport - Scheduler"
    const val SCHEDULER_NOTIFICATION_ID = 3

}