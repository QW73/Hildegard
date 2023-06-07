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
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.databinding.FragmentCartBinding
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
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                cartAdapter = CartAdapter(viewModel.getDishForCart())
                viewBinding.recyclerViewCart.adapter = cartAdapter
                viewBinding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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