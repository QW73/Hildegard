package com.qw73.hildegard.screens.main.profile.personalData


import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.qw73.hildegard.R
import com.qw73.hildegard.databinding.FragmentPersonalDataBinding
import com.qw73.hildegard.screens.main.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PersonalDataFragment : Fragment() {

    private lateinit var viewBinding: FragmentPersonalDataBinding
    private lateinit var datePickerDialog: DatePickerDialog

    /* view mode object */
    private val viewModel: PersonalDataViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        bindViews()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        restoreEditTextValues()
        viewBinding.etBirthday.setOnClickListener {
            showDatePicker()
        }
    }


    private fun setupObservers() {
        viewModel.birthday.observe(viewLifecycleOwner) { birthday ->
            if (!birthday.isNullOrEmpty()) {
                viewBinding.etBirthday.setText(birthday)
            }
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) { isValidEmail ->
            viewBinding.etEmail.error = if (!isValidEmail) {
                getString(R.string.invalid_email_error)
            } else {
                null
            }
        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
            if (!phoneNumber.isNullOrEmpty()) {
                // Найдите etPhone и установите номер телефона
                viewBinding.etPhone.setText(phoneNumber)
            }
        }
    }

    private fun restoreEditTextValues() {
        val savedName = sharedViewModel.getSavedName()
        val savedEmail = sharedViewModel.getSavedEmail()
        val savedBirthday = sharedViewModel.getSavedBirthday()

        viewBinding.etName.setText(savedName)
        viewBinding.etEmail.setText(savedEmail)
        viewBinding.etBirthday.setText(savedBirthday)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveEditTextValues()
    }

    private fun saveEditTextValues() {
        val name = viewBinding.etName.text.toString()
        val email = viewBinding.etEmail.text.toString()
        val birthday = viewBinding.etBirthday.text.toString()

        sharedViewModel.saveName(name)
        sharedViewModel.saveBirthday(birthday)

        if (viewModel.isValidEmail.value == true) {
            sharedViewModel.saveEmail(email)
        }
    }

    override fun onResume() {
        super.onResume()
        clearEmailError()
    }

    private fun clearEmailError() {
        if (viewModel.isValidEmail.value == false) {
            viewBinding.etEmail.error = null
        }
    }

    private fun bindViews() {
        viewBinding.etName.setText(sharedViewModel.getSavedName())
        viewBinding.etEmail.setText(sharedViewModel.getSavedEmail())
        viewBinding.etBirthday.setText(sharedViewModel.getSavedBirthday())

        viewBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.saveName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (email.isNotEmpty()) {
                    viewModel.checkEmailValidity(email)
                    if (viewModel.isValidEmail.value == true) {
                        sharedViewModel.saveEmail(email)
                        viewBinding.etEmail.error = null
                    } else {
                        viewBinding.etEmail.error = getString(R.string.invalid_email_error)
                    }
                } else {
                    viewBinding.etEmail.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.etBirthday.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedViewModel.saveBirthday(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        // Применяем локализацию к конфигурации
        activity?.resources?.updateConfiguration(config, activity?.resources?.displayMetrics)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                viewModel.setBirthday(formattedDate)

                if(sharedViewModel.getOneMore() == true)
                {
                    sharedViewModel.saveIsDataFull(true)
                    sharedViewModel.saveOneMore(false)

                    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                    val savedBirthday = sharedViewModel.getSavedBirthday()
                    if (savedBirthday != null) {
                        if (savedBirthday.isNotEmpty()) {
                            val birthday = dateFormat.parse(savedBirthday)
                            val age = viewModel.calculateAge(birthday)
                            sharedViewModel.saveAge(age.toString())
                        }
                    }
                    sharedViewModel.calculateDiet()

                }
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    companion object Companion {
        fun instance(): PersonalDataFragment {
            return PersonalDataFragment()
        }
    }
}