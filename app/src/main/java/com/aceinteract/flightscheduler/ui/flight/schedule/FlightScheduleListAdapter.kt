package com.aceinteract.flightscheduler.ui.flight.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.databinding.FlightScheduleListItemBinding
import com.aceinteract.flightscheduler.ui.flight.FlightListAdapter

/**
 * RecyclerView list adapter for airports.
 */
class FlightScheduleListAdapter(private val onClickCallback: (FlightSchedule) -> Unit) :
    ListAdapter<FlightSchedule, FlightScheduleListAdapter.ViewHolder>(FlightScheduleDiffCallback()) {

    /**
     * Inflate view and create view holder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(FlightScheduleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    /**
     * Bind data to view holder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.apply {
            bind(onClickCallback, task)
            itemView.tag = task
        }
    }

    /**
     * Holds view for recycling.
     */
    class ViewHolder(private val binding: FlightScheduleListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Setup item view.
         */
        fun bind(onClickCallback: (FlightSchedule) -> Unit, item: FlightSchedule) {

            val dividerItemDecoration = DividerItemDecoration(
                binding.root.context,
                LinearLayoutManager.VERTICAL
            )
            binding.apply {
                root.setOnClickListener { onClickCallback(item) }
                recyclerFlights.adapter = FlightListAdapter()
                recyclerFlights.addItemDecoration((dividerItemDecoration))
                schedule = item
                executePendingBindings()
            }
        }
    }

    /**
     * Callback for checking difference between two airport objects.
     */
    class FlightScheduleDiffCallback : DiffUtil.ItemCallback<FlightSchedule>() {

        /**
         * Check if items are the same.
         */
        override fun areItemsTheSame(oldItem: FlightSchedule, newItem: FlightSchedule) = oldItem == newItem

        /**
         * Check if contents are the same.
         */
        override fun areContentsTheSame(oldItem: FlightSchedule, newItem: FlightSchedule) = oldItem == newItem

    }
}

/**
 * Updates items in flight schedule list adapter
 */
@BindingAdapter("items")
fun RecyclerView.setItems(items: List<FlightSchedule>?) {
    if (items != null) {
        with(adapter as FlightScheduleListAdapter) {
            submitList(items)
            notifyDataSetChanged()
        }
    }
}