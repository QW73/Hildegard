package com.qw73.hildegard.screens.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.databinding.ItemCartBinding

class CartAdapter(private var dishes: List<Dish>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val dish = dishes[position]
        holder.bind(dish)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dish: Dish) {
            // Bind dish data to the views in the item layout
            binding.textCartName.text = dish.name
            binding.textCartCount.text = dish.count.toString()

        }
    }
}