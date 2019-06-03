package com.aceinteract.flightscheduler.ui.base

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * Base Fragment class that all Fragments use.
 *
 * Contains utility functions for use in the fragments.
 */
abstract class BaseFragment : Fragment() {

    /**
     * Casts fragment activity to AppCompatActivity.
     */
    protected val appCompatActivity: AppCompatActivity
        get() = (activity!! as AppCompatActivity)

    /**
     * Checks if on API 23 or above and if so, checks if it has the requested permission.
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(
            context!!,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}