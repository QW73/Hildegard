package com.qw73.hildegard.screens.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.databinding.ItemDishBinding

class DishAdapter(private var dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDishBinding.inflate(inflater, parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.bind(dish)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    inner class DishViewHolder(private val binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dish: Dish) {
            // Bind dish data to the views in the item layout
            binding.textViewName.text = dish.name

            Glide.with(binding.root.context)
                .load(dish.image)
                .into(binding.imageView)

            // Example: Set click listener on the item view
            binding.root.setOnClickListener {
                // Handle item click event
            }
        }
    }
}