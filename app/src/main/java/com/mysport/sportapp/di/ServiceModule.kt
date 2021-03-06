package com.mysport.sportapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.mysport.sportapp.R
import com.mysport.sportapp.constant.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.mysport.sportapp.constant.Constant.BASE_NOTIFICATION_CHANNEL_ID
import com.mysport.sportapp.constant.Constant.BASE_NOTIFICATION_CHANNEL_TITLE
import com.mysport.sportapp.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
            @ApplicationContext app: Context
    ): PendingIntent =
            PendingIntent.getActivity(
                    app,
                    0,
                    Intent(app, MainActivity::class.java).also {
                        it.action = ACTION_SHOW_TRACKING_FRAGMENT
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
            @ApplicationContext app: Context,
            pendingIntent: PendingIntent
    ): NotificationCompat.Builder =
            NotificationCompat.Builder(app, BASE_NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                    .setContentTitle(BASE_NOTIFICATION_CHANNEL_TITLE)
                    .setContentText("New message for you, expand this")
                    .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
            @ApplicationContext app: Context
    ) = FusedLocationProviderClient(app)

}
