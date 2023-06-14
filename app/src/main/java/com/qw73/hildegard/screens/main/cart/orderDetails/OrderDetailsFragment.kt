package com.qw73.hildegard.screens.main.cart.orderDetails


import android.app.DatePickerDialog
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.qw73.hildegard.R
import com.qw73.hildegard.data.api.AddressResponse
import com.qw73.hildegard.data.api.DadataApi
import com.qw73.hildegard.data.bd.dish.Dish
import com.qw73.hildegard.data.bd.dish.DishDao
import com.qw73.hildegard.data.bd.dish.Order
import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.databinding.FragmentOrderDetailsBinding
import com.qw73.hildegard.screens.main.cart.CartAdapter
import com.qw73.hildegard.screens.main.profile.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    @Inject
    lateinit var dadataApi: DadataApi

    @Inject
    lateinit var dishDao: DishDao

    private lateinit var viewBinding: FragmentOrderDetailsBinding

    private lateinit var datePickerDialog: DatePickerDialog

    private lateinit var cartAdapter: CartAdapter
    private lateinit var addressSuggestionsAdapter: AddressSuggestionsAdapter
    private var isAddressFocused: Boolean = false

    private lateinit var phoneNumber: String

    @Inject
    lateinit var pref: IPref

    /* view model object */
    private val viewModel: OrderDetailsViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        phoneNumber = pref.str("phone_number", "")
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        setupObservers()
        bindAdapter()
        viewBinding.etOrderDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupObservers() {
        viewModel.orderDate.observe(viewLifecycleOwner) { orderDate ->
            if (!orderDate.isNullOrEmpty()) {
                viewBinding.etOrderDate.setText(orderDate)
            }
        }
    }

    private fun bindAdapter() {
        // Initialize the adapter for the address suggestions list
        addressSuggestionsAdapter = AddressSuggestionsAdapter { selectedAddress ->
            // Handle the selected address
            viewBinding.etAddress.setText(selectedAddress)
            viewBinding.etAddress.clearFocus()
            viewBinding.rvAddressSuggestions.visibility = View.GONE
        }

        // Set the adapter for RecyclerView
        viewBinding.rvAddressSuggestions.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvAddressSuggestions.adapter = addressSuggestionsAdapter
    }

    private fun bindViews() {
        viewBinding.etAddress.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        viewBinding.etAddress.setOnFocusChangeListener { _, hasFocus ->
            isAddressFocused = hasFocus
            if (isAddressFocused && viewBinding.etAddress.text.isNotEmpty()) {
                viewBinding.rvAddressSuggestions.visibility = View.VISIBLE
            } else {
                viewBinding.rvAddressSuggestions.visibility = View.GONE
            }
        }

        viewBinding.etAddress.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewBinding.etAddress.clearFocus()
                viewBinding.rvAddressSuggestions.visibility = View.GONE
                true
            } else {
                false
            }
        }

        viewBinding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val address = s.toString()
                Log.d("AddressSuggestions", "Текст изменен: $address")
                if (isAddressFocused && address.isNotEmpty()) {
                    viewBinding.rvAddressSuggestions.visibility = View.VISIBLE
                    getAddressSuggestions(address)
                } else {
                    viewBinding.rvAddressSuggestions.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        viewBinding.buttonCartDetailsConfirm.setOnClickListener {
            if (viewBinding.etAddress.text.isNotEmpty() && viewBinding.etLevel.text.isNotEmpty() && viewBinding.etApartment.text.isNotEmpty() && viewBinding.etOrderDate.text.isNotEmpty()
                && !(viewModel.orderTime.value.isNullOrEmpty())  && !(viewModel.orderWeeks.value == 0 || viewModel.orderWeeks.value == null))
            {
                viewLifecycleOwner.lifecycleScope.launch {
                    val dishList = viewModel.getDishForCart()

                    // Сохранение данных в базе данных асинхронно
                    val isDataSaved = suspendCoroutine<Boolean> { continuation ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            for (dish in dishList) {
                                populateDatabase(dishDao, dish.name, dish.count, (dish.count * dish.price))
                                viewModel.updateDishCount(dish.id, 0)
                            }
                            continuation.resume(true)
                        }
                    }

                    // Переход к следующему фрагменту после сохранения данных
                    if (isDataSaved) {
                        val navController = Navigation.findNavController(requireView())
                        navController.navigate(R.id.action_order_detailsFragment_to_cartFragment)
                    }
                }
            }
            else {
                Toast.makeText(context, "Введите все необходимые данные", Toast.LENGTH_SHORT).show()
            }
        }


        viewBinding.radioGroupOrderTime.setOnCheckedChangeListener { _, checkedId ->
            val time = when (checkedId) {
                R.id.radio_day -> "9:00 - 14:00"
                R.id.radio_evening-> "16:00 - 21:00"
                else -> ""
            }
            viewModel.setOrderTime(time)
        }

        viewBinding.radioGroupWeeks.setOnCheckedChangeListener { _, checkedId ->
            val weeks = when (checkedId) {
                R.id.radio_4weeks-> 4
                R.id.radio_2weeks-> 2
                else -> 0
            }
            viewModel.setOrderWeeks(weeks)
        }
    }


    private suspend fun populateDatabase(dishDao: DishDao,dishName: String, dishCount:Int,price:Int) {

        val order = Order(

        phone = phoneNumber,
        date = viewBinding.etOrderDate.text.toString(),
        time = viewModel.orderTime.value.toString(),
        street = viewBinding.etAddress.text.toString(),
        floor = viewBinding.etLevel.text.toString(),
        apartment = viewBinding.etApartment.text.toString(),
        dish = dishName,
        dishCount =dishCount,
        totalPrice = price
        )
        dishDao.insertOrders(order)
    }

    private fun getAddressSuggestions(query: String) {
        val modifiedQuery = if (query.isBlank()) {
            "Ростов-на-Дону"
        } else {
            "Ростов-на-Дону $query"
        }
        val call = dadataApi.getAddressSuggestions(modifiedQuery, 5)
        call.enqueue(object : Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if (response.isSuccessful) {
                    val suggestions = response.body()?.suggestions
                    if (suggestions.isNullOrEmpty()) {
                        Log.d("AddressSuggestions", "Получены пустые предложения адресов")
                    } else {
                        val addressSuggestions =
                            suggestions.map { it.value.replaceFirst("Ростов-на-Дону", "") }
                        addressSuggestionsAdapter.submitList(addressSuggestions)
                    }
                } else {
                    Log.d(
                        "AddressSuggestions",
                        "Ошибка при получении предложений адресов: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                // Handle errors
            }
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

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                viewModel.setOrderDate(formattedDate)
            },
            year, month, day
        )

        // Установка минимальной доступной даты (2 дня вперед)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.DAY_OF_YEAR, 1)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        // Установка максимальной доступной даты (больше текущей даты)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 100)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    companion object {
        fun instance(): OrderDetailsFragment {
            return OrderDetailsFragment()
        }
    }
}
