package com.aceinteract.flightscheduler.ui.flight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.databinding.FlightListItemBinding

/**
 * RecyclerView list adapter for airports.
 */
class FlightListAdapter : ListAdapter<FlightSchedule.Flight, FlightListAdapter.ViewHolder>(FlightDiffCallback()) {

    /**
     * Inflate view and create view holder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(FlightListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    /**
     * Bind data to view holder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.apply {
            bind(task)
            itemView.tag = task
        }
    }

    /**
     * Holds view for recycling.
     */
    class ViewHolder(private val binding: FlightListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Setup item view.
         */
        fun bind(item: FlightSchedule.Flight) {
            binding.apply {
                flight = item
                executePendingBindings()
            }
        }
    }

    /**
     * Callback for checking difference between two airport objects.
     */
    class FlightDiffCallback : DiffUtil.ItemCallback<FlightSchedule.Flight>() {

        /**
         * Check if items are the same.
         */
        override fun areItemsTheSame(oldItem: FlightSchedule.Flight, newItem: FlightSchedule.Flight) =
            oldItem == newItem

        /**
         * Check if contents are the same.
         */
        override fun areContentsTheSame(oldItem: FlightSchedule.Flight, newItem: FlightSchedule.Flight) =
            oldItem == newItem

    }
}


/**
 * Updates items in flight list adapter
 */
@BindingAdapter("items")
fun RecyclerView.setItems(items: List<FlightSchedule.Flight>?) {
    if (items != null) {
        with(adapter as FlightListAdapter) {
            submitList(items)
            notifyDataSetChanged()
        }
    }
}