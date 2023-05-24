package com.qw73.hildegard.screens.main.profile.parameters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentParametersBinding
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ParametersFragment : Fragment() {

    lateinit var viewBinding: FragmentParametersBinding


    /* view mode object */
    private val viewModel: ParametersViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentParametersBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        restoreValues()
<<<<<<< HEAD

=======
        
>>>>>>> ff299308376b45f1e517a6f2b195fcbc6ad49e0b
    }

    private fun setupObservers() {
    }

    private fun restoreValues() {
        val savedAge = sharedViewModel.getSavedAge()
        val savedHeight = sharedViewModel.getSavedHeight()
        val savedWeight= sharedViewModel.getSavedWeight()

        val savedBirthday = sharedViewModel.getSavedBirthday()

        if (savedBirthday != null) {

            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            val birthday = dateFormat.parse(savedBirthday)

            val age = viewModel.calculateAge(birthday)
            viewBinding.etAge.setText(age.toString())
            viewBinding.etAge.isEnabled = false
        } else {
            viewBinding.etAge.setText(savedAge)
            viewBinding.etAge.isEnabled = true
        }

        viewBinding.etHeight.setText(savedHeight)
        viewBinding.etWeight.setText(savedWeight)

        when (sharedViewModel.getSavedGender()) {
            "Male" -> viewBinding.radioGroupGender.check(R.id.radio_male)
            "Female" -> viewBinding.radioGroupGender.check(R.id.radio_female)
        }

        when (sharedViewModel.getSavedActivity()) {
            "level1" -> viewBinding.radioGroupActivityLevel.check(R.id.radio_activity_level_1)
            "level2" -> viewBinding.radioGroupActivityLevel.check(R.id.radio_activity_level2)
            "level3" -> viewBinding.radioGroupActivityLevel.check(R.id.radio_activity_level3)
            "level4" -> viewBinding.radioGroupActivityLevel.check(R.id.radio_activity_level4)
        }

        when (sharedViewModel.getSavedGoals()) {
            "goals1" -> viewBinding.radioGroupGoals.check(R.id.radio_goals1)
            "goals2" -> viewBinding.radioGroupGoals.check(R.id.radio_goals2)
            "goals3" -> viewBinding.radioGroupGoals.check(R.id.radio_goals3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveValues()
    }

    private fun saveValues() {
        val age = viewBinding.etAge.text.toString()
        val height = viewBinding.etHeight.text.toString()
        val weight = viewBinding.etWeight.text.toString()
        val savedBirthday = sharedViewModel.getSavedBirthday()

        if (savedBirthday == null && age.isNotEmpty() && age.toInt() in 1..123) {
            sharedViewModel.saveAge(age)
        }

        if (height.isNotEmpty() && height.toInt() in 1..272) {
            sharedViewModel.saveHeight(height)
        }

        if (weight.isNotEmpty() && weight.toInt() in 1..635) {
            sharedViewModel.saveWeight(weight)
        }
    }

    private fun bindViews() {

        viewBinding.etAge.setText(sharedViewModel.getSavedAge())
        viewBinding.etHeight.setText(sharedViewModel.getSavedHeight())
        viewBinding.etWeight.setText(sharedViewModel.getSavedWeight())

        viewBinding.etAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.saveAge(s.toString())
                checkDataFullness()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.etHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.saveHeight(s.toString())
                checkDataFullness()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.saveWeight(s.toString())
                checkDataFullness()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

// Обработчик изменений для переключателей
        viewBinding.radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val gender = when (checkedId) {
                R.id.radio_male -> "Male"
                R.id.radio_female -> "Female"
                else -> ""
            }
            sharedViewModel.saveGender(gender)
            checkDataFullness()
        }

        viewBinding.radioGroupActivityLevel.setOnCheckedChangeListener { group, checkedId ->
            val activity = when (checkedId) {
                R.id.radio_activity_level_1 -> "level1"
                R.id.radio_activity_level2 -> "level2"
                R.id.radio_activity_level3 -> "level3"
                R.id.radio_activity_level4 -> "level4"
                else -> ""
            }
            sharedViewModel.saveActivity(activity)
            checkDataFullness()
        }

        viewBinding.radioGroupGoals.setOnCheckedChangeListener { group, checkedId ->
            val goals = when (checkedId) {
                R.id.radio_goals1 -> "goals1"
                R.id.radio_goals2  -> "goals2"
                R.id.radio_goals3  -> "goals3"
                else -> ""
            }
            sharedViewModel.saveGoals(goals)
            checkDataFullness()
        }
    }

    private fun checkDataFullness() {
        val age = viewBinding.etAge.text?.toString()
        val height = viewBinding.etHeight.text?.toString()
        val weight = viewBinding.etWeight.text?.toString()
        val gender = sharedViewModel.getSavedGender()
        val activity = sharedViewModel.getSavedActivity()
        val goals = sharedViewModel.getSavedGoals()

        val isDataFull = !age.isNullOrBlank() && !height.isNullOrBlank() && !weight.isNullOrBlank()
                && !gender.isNullOrBlank() && !activity.isNullOrBlank() && !goals.isNullOrBlank()

<<<<<<< HEAD
        val oneMore = !height.isNullOrBlank() && !weight.isNullOrBlank()
                && !gender.isNullOrBlank() && !activity.isNullOrBlank() && !goals.isNullOrBlank()

        sharedViewModel.saveOneMore(oneMore)

=======
>>>>>>> ff299308376b45f1e517a6f2b195fcbc6ad49e0b
        viewModel.setIsDataFull(isDataFull)
        sharedViewModel.saveIsDataFull(isDataFull)
    }

    companion object Companion {
        fun instance(): ParametersFragment {
            return ParametersFragment()
        }
    }
}