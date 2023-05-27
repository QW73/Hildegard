package com.qw73.hildegard.screens.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.qw73.hildegard.R
import com.qw73.hildegard.data.bd.AppDatabase
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.data.bd.DishDao
import com.qw73.hildegard.databinding.FragmentHomeBinding
import com.qw73.hildegard.screens.main.DishAdapter
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var viewBinding: FragmentHomeBinding

    /* view mode object */
    private val viewModel: HomeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var dishAdapter1: DishAdapter
    private lateinit var dishAdapter2: DishAdapter
    private lateinit var dishAdapter3: DishAdapter

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

        // Check if the exclusions list is empty
        lifecycleScope.launch {
            val category1Dishes = if (viewModel.exclusions.isEmpty()) {
                viewModel.getDishesByCategory("Category 1")
            } else {
                viewModel.getDishesByCategoryWithExclusions("Category 1",viewModel.exclusions)
            }
            dishAdapter1 = DishAdapter(category1Dishes)
            viewBinding.recyclerView.adapter = dishAdapter1
            viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            // Действия с полученным списком блюд для recyclerView1

            val category2Dishes = if (viewModel.exclusions.isEmpty()) {
                viewModel.getDishesByCategory("Category 2")

            } else {
                viewModel.getDishesByCategoryWithExclusions("Category 2",viewModel.exclusions)
            }

            dishAdapter2 = DishAdapter(category2Dishes)
            viewBinding.recyclerView2.adapter = dishAdapter2
            viewBinding.recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            // Действия с полученным списком блюд для recyclerView2

            val category3Dishes = if (viewModel.exclusions.isEmpty()) {
                viewModel.getDishesByCategory("Category 3")
            } else {
                viewModel.getDishesByCategoryWithExclusions("Category 3",viewModel.exclusions)
            }
            dishAdapter3 = DishAdapter(category3Dishes)
            viewBinding.recyclerView3.adapter = dishAdapter3
            viewBinding.recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            // Действия с полученным списком блюд для recyclerView3
        }
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