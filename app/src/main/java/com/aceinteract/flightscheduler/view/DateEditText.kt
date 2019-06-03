package com.aceinteract.flightscheduler.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.FragmentActivity
import com.aceinteract.flightscheduler.view.dialog.TextDatePickerDialog
import com.aceinteract.flightscheduler.view.dialog.TextDatePickerDialog.Companion.DATE_PICKER_VALUE
import com.google.android.material.textfield.TextInputEditText

/**
 * Custom Edit Text view for setting a date
 */
class DateEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    private val fragment = TextDatePickerDialog()

    /**
     * Listener that can be changed to trigger a change in the date
     */
    var dateChangeListener: OnDateChangedListener = object : OnDateChangedListener {
        override fun onDateChanged(date: String) {
            // Nothing is to be done by default
        }
    }

    /**
     * Date selected in the widget in text
     */
    var dateText = ""

    init {
        keyListener = null
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showPicker()
            }
        }
        setOnClickListener {
            if (hasFocus()) showPicker()
        }

    }

    private fun showPicker() {
        val args = Bundle()
        args.putString(DATE_PICKER_VALUE, dateText)
        fragment.arguments = args
        fragment.listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            dateText = "$year-${(month + 1).toString().padStart(2, '0')}" +
                    "-${dayOfMonth.toString().padStart(2, '0')}"
            setText(dateText)
            dateChangeListener.onDateChanged(dateText)
        }
        fragment.show(
            ((context as ContextThemeWrapper).baseContext as FragmentActivity).supportFragmentManager,
            "datePicker"
        )
    }

    /**
     * Simple interface to check when the date is changed.
     */
    interface OnDateChangedListener {

        /**
         * Gets triggered with a date string when the date is changed.
         */
        fun onDateChanged(date: String)

    }
}

/**
 * Set a date change listener on a date edit text widget.
 */
@BindingAdapter("dateChangeListener")
fun DateEditText.setDateChangeListener(attrChange: InverseBindingListener) {
    dateChangeListener = object : DateEditText.OnDateChangedListener {
        override fun onDateChanged(date: String) {
            attrChange.onChange()
        }
    }
}

/**
 * Set the date text for the widget.
 */
@BindingAdapter("dateText")
fun DateEditText.setDateText(date: String?) {
    date?.let {
        if (it != dateText) {
            dateText = it
            setText(dateText)
        }
    }
}

/**
 * Inverse adapter to get the date from the date edit text widget.
 */
@InverseBindingAdapter(attribute = "dateText", event = "dateChangeListener")
fun DateEditText.getDateText() = dateText