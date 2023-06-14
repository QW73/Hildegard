package com.qw73.hildegard.screens.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentCartBinding
import com.qw73.hildegard.screens.main.profile.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var viewBinding: FragmentCartBinding

    private lateinit var cartAdapter: CartAdapter

    /* view mode object */
    private val viewModel: CartViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var totalPrice: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentCartBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAdapters()
        bindViews()
    }

    private fun bindAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            val dishes = withContext(Dispatchers.IO) {
                viewModel.getDishForCart()
            }
            var textSum= ""

            if (dishes.isEmpty()) {
                // Display empty view
                viewBinding.recyclerViewCart.visibility = View.GONE
                viewBinding.buttonCartAdd.visibility = View.GONE
                viewBinding.emptyCart.visibility = View.VISIBLE
            } else {
                // Display non-empty view
                cartAdapter = CartAdapter(dishes)
                viewBinding.recyclerViewCart.adapter = cartAdapter
                viewBinding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                totalPrice = dishes.sumOf { dish ->
                    dish.price * dish.count
                }

                if (sharedViewModel.savedCalories != null) {

                    val totalCalories= dishes.sumOf { dish ->
                        dish.calories * dish.count
                    }
                    val totalProtein = dishes.sumOf { dish ->
                        dish.proteins * dish.count
                    }
                    val totalFat = dishes.sumOf { dish ->
                        dish.fats * dish.count
                    }
                    val totalCarbohydrates = dishes.sumOf { dish ->
                        dish.carbohydrates * dish.count
                    }

                    textSum = "Сумма "

                    if(sharedViewModel.savedCalories != totalCalories)
                    {
                        if(sharedViewModel.savedCalories!! > totalCalories)
                        {
                            textSum += "калорий меньше нормы на " + (sharedViewModel.savedCalories!! -totalCalories)+" кКал"
                        }
                        if(sharedViewModel.savedCalories!! < totalCalories)
                        {
                            textSum += "калорий больше нормы на " + (totalCalories -sharedViewModel.savedCalories!! )+" кКал"
                        }
                    } else textSum += "калорий соответсвует норме "

                    if(sharedViewModel.savedProtein != totalProtein)
                    {
                        if(sharedViewModel.savedProtein!! > totalProtein)
                        {
                            textSum += ", белка меньше на " + (sharedViewModel.savedProtein!! -totalProtein)+" г"
                        }
                        if(sharedViewModel.savedCalories!! < totalCalories)
                        {
                            textSum += ", белка больше на " + (totalProtein - sharedViewModel.savedProtein!!)+" г"
                        }
                    } else textSum += ", белка соответсвует норме"

                    if(sharedViewModel.savedFat != totalFat)
                    {
                        if(sharedViewModel.savedFat!! > totalFat)
                        {
                            textSum += ", жиров меньше " + (sharedViewModel.savedFat!! -totalFat)+" г"
                        }
                        if(sharedViewModel.savedCalories!! < totalCalories)
                        {
                            textSum += ", жиров больше на " + (totalFat - sharedViewModel.savedFat!!)+" г"
                        }
                    } else textSum += ", жиров соответсвует норме"

                    if(sharedViewModel.savedCarbohydrates != totalCarbohydrates)
                    {
                        if(sharedViewModel.savedCarbohydrates!! > totalCarbohydrates)
                        {
                            textSum += ", углеводов меньше на " + (sharedViewModel.savedCarbohydrates!! -totalCarbohydrates)+" г."
                        }
                        if(sharedViewModel.savedCalories!! < totalCalories)
                        {
                            textSum += ", углеводов больше на " + (totalCarbohydrates- sharedViewModel.savedCarbohydrates!!)+" г."
                        }
                    } else textSum += ", углеводов соответсвует норме."

                }
                viewBinding.tvWarning.text = textSum
                viewBinding.buttonCartAdd.text = "Оформить заказ\n${totalPrice} ₽"

                viewBinding.recyclerViewCart.visibility = View.VISIBLE
                viewBinding.buttonCartAdd.visibility = View.VISIBLE
                viewBinding.emptyCart.visibility = View.GONE
            }
        }
    }



    private fun bindViews() {
        viewBinding.buttonCartAdd.setOnClickListener {

            val navController = Navigation.findNavController(requireView())

            navController.navigate(R.id.action_cartFragment_to_order_detailsFragment)
        }

    }

    companion object Companion {
        fun instance(): CartFragment {
            return CartFragment()
        }
    }
}