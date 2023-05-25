package com.qw73.hildegard.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    lateinit var viewBinding: FragmentOrdersBinding

    private val viewModel: OrdersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentOrdersBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }


    private fun bindViews() {

    }

    companion object Companion {
        fun instance(): OrdersFragment {
            return OrdersFragment()
        }
    }
}