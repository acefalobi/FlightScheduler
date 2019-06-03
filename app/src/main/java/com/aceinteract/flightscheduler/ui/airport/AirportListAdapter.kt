package com.aceinteract.flightscheduler.ui.airport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.databinding.AirportListItemBinding

/**
 * RecyclerView list adapter for airports.
 */
class AirportListAdapter(private val onClickCallback: (Airport) -> Unit) :
    ListAdapter<Airport, AirportListAdapter.ViewHolder>(AirportDiffCallback()) {

    /**
     * Inflate view and create view holder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(AirportListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

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
    class ViewHolder(private val binding: AirportListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Setup item view.
         */
        fun bind(onClickCallback: (Airport) -> Unit, item: Airport) {
            binding.apply {
                root.setOnClickListener { onClickCallback(item) }
                airport = item
                executePendingBindings()
            }
        }
    }

    /**
     * Callback for checking difference between two airport objects.
     */
    class AirportDiffCallback : DiffUtil.ItemCallback<Airport>() {

        /**
         * Check if items are the same.
         */
        override fun areItemsTheSame(oldItem: Airport, newItem: Airport) = oldItem.code == newItem.code

        /**
         * Check if contents are the same.
         */
        override fun areContentsTheSame(oldItem: Airport, newItem: Airport) = oldItem == newItem

    }
}

/**
 * Updates items in airport list adapter
 */
@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Airport>?) {
    if (items != null) {
        with(adapter as AirportListAdapter) {
            submitList(items)
            notifyDataSetChanged()
        }
    }
}