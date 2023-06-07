package com.qw73.hildegard.screens.main.cart.orderDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qw73.hildegard.data.api.AddressSuggestion
import com.qw73.hildegard.databinding.ItemAddressSuggestionBinding

class AddressSuggestionsAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<String, AddressSuggestionsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddressSuggestionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.bind(suggestion)
    }

    inner class ViewHolder(private val binding: ItemAddressSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val suggestion = getItem(adapterPosition)
                val formattedAddress = suggestion.removePrefix("г , ")
                onItemClick(formattedAddress)
            }
        }

        fun bind(suggestion: String) {
            val formattedAddress = suggestion.removePrefix("г , ")
            binding.tvAddress.text = formattedAddress
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}