package com.aceinteract.flightscheduler

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

/**
 * Main home activity containing all fragments and navigation.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Set the activity view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Set home action.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController(R.id.frame_container).navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
