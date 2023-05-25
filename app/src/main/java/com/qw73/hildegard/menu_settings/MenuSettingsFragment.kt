package com.qw73.hildegard.menu_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.databinding.FragmentMenuSettingsBinding
import com.qw73.hildegard.screens.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuSettingsFragment : Fragment() {

    lateinit var viewBinding: FragmentMenuSettingsBinding

    private val viewModel: MenuSettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentMenuSettingsBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }


    private fun bindViews() {

    }

    companion object Companion {
        fun instance(): MenuSettingsFragment {
            return MenuSettingsFragment()
        }
    }
}