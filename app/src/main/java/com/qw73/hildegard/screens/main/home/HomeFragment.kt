package com.qw73.hildegard.screens.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentHomeBinding
import com.qw73.hildegard.screens.main.SharedViewModel
import com.qw73.hildegard.screens.main.profile.personalData.PersonalDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var viewBinding: FragmentHomeBinding

    /* view mode object */
    private val viewModel: HomeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        sharedViewModel.getSavedName()?.let { savedName ->
            sharedViewModel.setSavedName(savedName)
        }

        // Устанавливаем приветствие
        updateGreetingText()
    }

    private fun bindViews() {
        //
    }

    private fun setupObservers() {
        sharedViewModel.getSavedName()?.let { savedName ->
            val greetingText = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                in 6..11 -> getString(R.string.good_morning)
                in 12..17 -> getString(R.string.good_afternoon)
                in 18..21 -> getString(R.string.good_evening)
                else -> getString(R.string.good_night)
            }

            if (savedName.isNotEmpty()) {
                viewBinding.tvGreeting.text = getString(R.string.greetings_with_name, greetingText, savedName)
            } else {
                viewBinding.tvGreeting.text = greetingText
            }
        }

        sharedViewModel.name.observe(viewLifecycleOwner) { name ->
            updateGreetingText()
        }
    }

    private fun updateGreetingText() {
        val name = sharedViewModel.name.value
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingText = when (currentHour) {
            in 6..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            in 18..21 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
        }

        if (name != null && name.isNotEmpty()) {
            viewBinding.tvGreeting.text = getString(R.string.greetings_with_name,greetingText, name)
        } else {
            viewBinding.tvGreeting.text = getString(R.string.greetings,greetingText)
        }
    }

    companion object Companion {
        fun instance(): HomeFragment {
            return HomeFragment()
        }
    }
}