package com.qw73.hildegard.screens.main.home.exp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentExpDishBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExpDishFragment : Fragment() {

    lateinit var viewBinding: FragmentExpDishBinding
    private val viewModel: ExpDishViewModel by activityViewModels()
    private var dishId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentExpDishBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dishId = arguments?.getLong("dishId")
        dishId?.let { id ->
            lifecycleScope.launch {
                viewModel.getDishFlow(id).collect { dish ->
                    dish?.let {
                        // Обновление пользовательского интерфейса после получения данных
                        viewBinding.expDishName.text = getString(R.string.item_dish_name, dish.name)
                        viewBinding.expIngredients.text =
                            getString(R.string.exp_item_dish_ingredients,
                                dish.ingredients.joinToString(", "))
                        viewBinding.expProtein.text =
                            getString(R.string.exp_item_dish_protein, dish.proteins.toString())
                        viewBinding.expFat.text =
                            getString(R.string.exp_item_dish_fat, dish.fats.toString())
                        viewBinding.expCarbohydrates.text =
                            getString(R.string.exp_item_dish_carbohydrates,
                                dish.carbohydrates.toString())
                        viewBinding.expCalories.text =
                            getString(R.string.exp_item_dish_calories, dish.calories.toString())
                        viewBinding.expGram.text =
                            getString(R.string.exp_item_dish_gram, dish.grams.toString())
                        viewBinding.expPrice.text =
                            getString(R.string.item_dish_price, dish.price.toString())


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
                }
            }
        }

        viewBinding.buttonAdd.setOnClickListener {
            dishId?.let { id ->
                viewModel.updateDishCount(id, 1)
            }
        }

        viewBinding.icPlus.setOnClickListener {
            dishId?.let { id ->
                val dish = viewModel.getDishById(id)
                dish?.let { viewModel.updateDishCount(id, it.count + 1) }
            }
        }

        viewBinding.icMinus.setOnClickListener {
            dishId?.let { id ->
                val dish = viewModel.getDishById(id)
                dish?.let {
                    val newCount = it.count - 1
                    if (newCount > 0) {
                        viewModel.updateDishCount(id, newCount)
                    } else {
                        viewModel.updateDishCount(id, 0)
                        viewBinding.buttonAdd.visibility = View.VISIBLE
                        viewBinding.addButtonsLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        fun instance(): ExpDishFragment {
            return ExpDishFragment()
        }
    }
}
