package com.qw73.hildegard.screens.main.profile.menu_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentMenuSettingsBinding
import com.qw73.hildegard.screens.main.SharedViewModel
import com.qw73.hildegard.screens.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MenuSettingsFragment : Fragment() {

    lateinit var viewBinding: FragmentMenuSettingsBinding

    private val viewModel: MenuSettingsViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentMenuSettingsBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        restoreValues()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreValues()
    }

    private fun restoreValues() {

        when (sharedViewModel.getMilk()) {
            false -> viewBinding.radioGroupMilk.check(R.id.radio_milk_yes)
            true -> viewBinding.radioGroupMilk.check(R.id.radio_milk_no)
            else -> {}
        }

        when (sharedViewModel.getEggs()) {
            false -> viewBinding.radioGroupEggs.check(R.id.radio_eggs_yes)
            true -> viewBinding.radioGroupEggs.check(R.id.radio_eggs_no)
            else -> {}
        }

        when (sharedViewModel.getSugar()) {
            false -> viewBinding.radioSugar.check(R.id.radio_sugar_yes)
            true -> viewBinding.radioSugar.check(R.id.radio_sugar_no)
            else -> {}
        }

        when (sharedViewModel.getNuts()) {
            false -> viewBinding.radioNuts.check(R.id.radio_nuts_yes)
            true -> viewBinding.radioNuts.check(R.id.radio_nuts_no)
            else -> {}
        }

        when (sharedViewModel.getGluten()) {
            false -> viewBinding.radioGroupGluten.check(R.id.radio_gluten_yes)
            true -> viewBinding.radioGroupGluten.check(R.id.radio_gluten_no)
            else -> {}
        }
    }

    private fun bindViews() {

        viewBinding.radioGroupMilk.setOnCheckedChangeListener { _, checkedId ->
            val milk = when (checkedId) {
                R.id.radio_milk_yes -> false
                R.id.radio_milk_no -> true
                else -> {null}
            }
            if (milk != null) {
                sharedViewModel.saveMilk(milk)
            }
        }

        viewBinding.radioGroupEggs.setOnCheckedChangeListener { _, checkedId ->
            val eggs = when (checkedId) {
                R.id.radio_eggs_yes -> false
                R.id.radio_eggs_no -> true
                else -> {null}
            }
            if (eggs != null) {
                sharedViewModel.saveEggs(eggs)
            }
        }

        viewBinding.radioSugar.setOnCheckedChangeListener { _, checkedId ->
            val sugar = when (checkedId) {
                R.id.radio_sugar_yes -> false
                R.id.radio_sugar_no -> true
                else -> {null}
            }
            if (sugar != null) {
                sharedViewModel.saveSugar(sugar)
            }
        }

        viewBinding.radioNuts.setOnCheckedChangeListener { _, checkedId ->
            val nuts = when (checkedId) {
                R.id.radio_nuts_yes -> false
                R.id.radio_nuts_no -> true
                else -> {null}
            }
            if (nuts != null) {
                sharedViewModel.saveNuts(nuts)
            }
        }

        viewBinding.radioGroupGluten.setOnCheckedChangeListener { _, checkedId ->
            val gluten = when (checkedId) {
                R.id.radio_gluten_yes -> false
                R.id.radio_gluten_no -> true
                else -> {null}
            }
            if (gluten != null) {
                sharedViewModel.saveGluten(gluten)
            }
        }
    }

    companion object Companion {
        fun instance(): MenuSettingsFragment {
            return MenuSettingsFragment()
        }
    }
}