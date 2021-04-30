package com.mysport.sportapp.ui.main

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mysport.sportapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainNavHostFragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            R.id.navigation_home,
            R.id.navigation_news,
            R.id.navigation_tracker,
            R.id.navigation_history,
            R.id.navigation_scheduler
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)

        mainBottomNavView.setupWithNavController(navController)

        navController
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.navigation_home,
                    R.id.navigation_news, R.id.navigation_tracker,
                    R.id.navigation_history, R.id.navigation_scheduler ->
                        mainBottomNavView.visibility = View.VISIBLE
                    else -> mainBottomNavView.visibility = View.GONE
                }
            }
    }
}