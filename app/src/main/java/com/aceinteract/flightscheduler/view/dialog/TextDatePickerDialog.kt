package com.aceinteract.flightscheduler.view.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import com.aceinteract.flightscheduler.util.DateTimeUtil
import java.util.*

/**
 * Simple date picker dialog.
 */
class TextDatePickerDialog : androidx.fragment.app.DialogFragment() {

    private val date: Calendar? by lazy { DateTimeUtil.stringToDate(arguments?.getString(DATE_PICKER_VALUE)!!) }

    /**
     * Listener for when the date is set.
     */
    lateinit var listener: DatePickerDialog.OnDateSetListener

    /**
     * Setup dialog with appropriate parameters.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = date ?: Calendar.getInstance()

        return DatePickerDialog(
            context!!, listener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    companion object {
        /**
         * Key for storing and retrieving date.
         */
        const val DATE_PICKER_VALUE = "date_picker_dob"
    }

}