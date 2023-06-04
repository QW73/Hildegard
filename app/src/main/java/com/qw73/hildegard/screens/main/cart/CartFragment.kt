package com.qw73.hildegard.screens.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.qw73.hildegard.databinding.FragmentCartBinding
import com.qw73.hildegard.databinding.FragmentProfileBinding
import com.qw73.hildegard.screens.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var viewBinding: FragmentCartBinding



    /* view mode object */
    private val viewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentCartBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }


    private fun bindViews() {

    }

    companion object Companion {
        fun instance(): CartFragment {
            return CartFragment()
        }
    }
}