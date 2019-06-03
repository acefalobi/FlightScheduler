package com.aceinteract.flightscheduler.ui.base

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel

/**
 * Base class for all view model classes.
 * It contains shared fields common to most view models in the application
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Observable boolean that notifies view whether content is loading or not.
     */
    val isLoading = ObservableBoolean(false)

}