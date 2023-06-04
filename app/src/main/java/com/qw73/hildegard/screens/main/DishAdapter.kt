package com.qw73.hildegard.screens.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qw73.hildegard.R
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.databinding.ItemDishBinding
import com.qw73.hildegard.screens.main.home.exp.ExpDishFragment

class DishAdapter(private var dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    private lateinit var onDishClickListener: (Dish) -> Unit

    fun setOnDishClickListener(listener: (Dish) -> Unit) {
        onDishClickListener = listener
    }

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

        init {
            setupClickListener()
        }

        private fun setupClickListener() {
            binding.root.setOnClickListener {
                val dish = dishes[adapterPosition]
                onDishClickListener.invoke(dish)
            }

            binding.root.setOnLongClickListener {
                val dish = dishes[adapterPosition]
                toggleExpandItem()
                true
            }
        }

        private fun toggleExpandItem() {
            val expandedLayoutVisibility = binding.expandedLayout.visibility
            if (expandedLayoutVisibility == View.VISIBLE) {
                binding.expandedLayout.visibility = View.GONE
            } else {
                binding.expandedLayout.visibility = View.VISIBLE
            }
        }

        fun bind(dish: Dish) {
            // Bind dish data to the views in the item layout
            binding.textViewName.text = dish.name
            binding.textViewPrice.text = dish.price.toString() + " ₽"
            binding.textViewGram.text = dish.grams.toString() + " гр"
            binding.textViewProtein.text = dish.proteins.toString() + " гр"
            binding.textViewFat.text = dish.fats.toString() + " гр"
            binding.textViewCarbohydrates.text = dish.carbohydrates.toString() + " гр"
            binding.textViewCalories.text = dish.calories.toString() +" кКал"

            Glide.with(binding.root.context)
                .load(dish.image)
                .into(binding.imageView)

            // Hide the expanded layout initially
            binding.expandedLayout.visibility = View.GONE
        }
    }
}