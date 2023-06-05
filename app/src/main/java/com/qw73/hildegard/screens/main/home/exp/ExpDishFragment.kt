package com.qw73.hildegard.screens.main.home.exp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.qw73.hildegard.R
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.databinding.FragmentExpDishBinding
import com.qw73.hildegard.databinding.FragmentProfileBinding
import com.qw73.hildegard.screens.main.cart.CartViewModel
import com.qw73.hildegard.screens.main.profile.ProfileFragment
import com.qw73.hildegard.screens.main.profile.ProfileViewModel

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExpDishFragment : Fragment() {

    lateinit var viewBinding: FragmentExpDishBinding

    /* view mode object */
    private val viewModel: ExpDishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentExpDishBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dishId = arguments?.getLong("dishId")
        if (dishId != null) {
            val dishLiveData = viewModel.getDishLiveData(dishId)
            dishLiveData.observe(viewLifecycleOwner, Observer { dish ->
                dish?.let {
                    // Используйте информацию о блюде для обновления пользовательского интерфейса
                    viewBinding.expDishName.text = dish.name
                    viewBinding.expIngredients.text = dish.ingredients.joinToString(", ")
                    viewBinding.expProtein.text = dish.proteins.toString() + " г"
                    viewBinding.expFat.text = dish.fats.toString() + " г"
                    viewBinding.expCarbohydrates.text = dish.carbohydrates.toString() + " г"
                    viewBinding.expCalories.text = dish.calories.toString() + " кКал"
                    viewBinding.expGram.text = "  / " + dish.grams.toString() + " грамм"
                    viewBinding.expPrice.text = dish.price.toString() + " ₽"

                    Glide.with(viewBinding.root.context)
                        .load(dish.image)
                        .into(viewBinding.iconForExpDish)

                    if (dish.count == 0) {
                        viewBinding.buttonAdd.visibility = View.VISIBLE
                        viewBinding.addButtonsLayout.visibility = View.GONE
                    } else {
                        viewBinding.buttonAdd.visibility = View.GONE
                        viewBinding.addButtonsLayout.visibility = View.VISIBLE
                        viewBinding.expCount.text = dish.count.toString()
                    }
                }
            })

            viewBinding.buttonAdd.setOnClickListener {
                viewModel.updateDishCount(dishId, 1)
            }

            viewBinding.icPlus.setOnClickListener {
                val dish = viewModel.getDishById(dishId)
                dish?.let { viewModel.updateDishCount(dishId, it.count + 1) }
            }

            viewBinding.icMinus.setOnClickListener {
                val dish = viewModel.getDishById(dishId)
                dish?.let {
                    val newCount = it.count - 1
                    if (newCount > 0) {
                        viewModel.updateDishCount(dishId, newCount)
                    } else {
                        viewModel.updateDishCount(dishId, 0)
                        viewBinding.buttonAdd.visibility = View.VISIBLE
                        viewBinding.addButtonsLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun bindViews() {
        // Bind your views here if needed
    }

    companion object {
        fun instance(): ExpDishFragment {
            return ExpDishFragment()
        }
    }
}
