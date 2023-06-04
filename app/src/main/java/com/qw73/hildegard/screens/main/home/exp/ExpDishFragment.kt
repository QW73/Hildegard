package com.qw73.hildegard.screens.main.home.exp

import android.app.ActionBar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.qw73.hildegard.R
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.databinding.FragmentExpDishBinding
import com.qw73.hildegard.databinding.FragmentProfileBinding
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
            val dish = viewModel.getDishById(dishId)
            if (dish != null) {
                // Используйте информацию о блюде для обновления пользовательского интерфейса
                viewBinding.expDishName.text = dish.name
                viewBinding.expIngredients.text = dish.ingredients.joinToString(", ")
                viewBinding.expProtein.text = dish.proteins.toString() + " г"
                viewBinding.expFat.text = dish.fats.toString() + " г"
                viewBinding.expCarbohydrates.text = dish.carbohydrates.toString() + " г"
                viewBinding.expCalories.text = dish.calories.toString() + " кКал"
                viewBinding.expGram.text = "  / "+ dish.grams.toString() + " грамм"
                viewBinding.expPrice.text = dish.price.toString() + " ₽"

                Glide.with( viewBinding.root.context)
                    .load(dish.image)
                    .into( viewBinding.iconForExpDish)

            // Другие поля блюда
            }
        }
    }

    private fun bindViews() {

    }

    companion object Companion {
        fun instance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}