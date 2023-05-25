package com.qw73.hildegard.screens.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var viewBinding: FragmentProfileBinding

    /* view mode object */
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }


    private fun bindViews() {
        viewBinding.containerPersonalData.setOnClickListener {
            viewModel.profileItemClick(it as LinearLayoutCompat)
        }

        viewBinding.containerParameters.setOnClickListener {
            viewModel.profileItemClick(it as LinearLayoutCompat)
        }

        viewBinding.containerOrders.setOnClickListener {
            viewModel.profileItemClick(it as LinearLayoutCompat)
        }

        viewBinding.containerMenuSettings.setOnClickListener {
            viewModel.profileItemClick(it as LinearLayoutCompat)
        }
    }

    companion object Companion {
        fun instance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}