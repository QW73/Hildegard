package com.qw73.hildegard.screens.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        bindViews()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindAdapters()
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

    }

    companion object Companion {
        fun instance(): CartFragment {
            return CartFragment()
        }
    }
}