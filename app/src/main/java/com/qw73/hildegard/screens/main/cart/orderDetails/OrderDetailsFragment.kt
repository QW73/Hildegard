package com.qw73.hildegard.screens.main.cart.orderDetails


import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.qw73.hildegard.data.api.AddressResponse
import com.qw73.hildegard.data.api.DadataApi
import com.qw73.hildegard.databinding.FragmentOrderDetailsBinding
import com.qw73.hildegard.screens.main.cart.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    @Inject
    lateinit var dadataApi: DadataApi

    private lateinit var viewBinding: FragmentOrderDetailsBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var addressSuggestionsAdapter: AddressSuggestionsAdapter
    private var isAddressFocused: Boolean = false

    /* view model object */
    private val viewModel: OrderDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        bindAdapter()
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

    companion object {
        fun instance(): OrderDetailsFragment {
            return OrderDetailsFragment()
        }
    }
}
