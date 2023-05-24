package com.qw73.hildegard.screens.main

import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.qw73.hildegard.R
import com.qw73.hildegard.base.BaseActivity
import com.qw73.hildegard.data.OPEN_PARAMETERS
import com.qw73.hildegard.data.OPEN_PERSONAL_DATA
import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.databinding.ActivityMainBinding
import com.qw73.hildegard.navigate.Navigator
import com.qw73.hildegard.screens.main.profile.ProfileViewModel
import com.qw73.hildegard.screens.main.profile.personalData.PersonalDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var phoneNumber: String

    @Inject
    lateinit var navigor: Navigator

    @Inject
    lateinit var pref: IPref

    @Inject
    lateinit var res: IRes

    private val profileViewModel: ProfileViewModel by viewModels()
    private val personalDataViewModel: PersonalDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = pref.str("phone_number", "")
        binding = viewBinding as ActivityMainBinding
        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
        bindViews()
        bindObservers()
    }

    override fun binding(): ViewBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private fun bindViews() {
        //
    }

    private fun bindObservers() {
        profileViewModel.profileEvents.observe(this) { event ->
            handleProfileEvent(event)
        }
    }

    private fun handleProfileEvent(event: Int) {
        when (event) {
            OPEN_PERSONAL_DATA -> {
                personalDataViewModel.setPhoneNumber(phoneNumber)
                navController.navigate(R.id.action_profileFragment_to_personal_data_Fragment)
            }

            OPEN_PARAMETERS -> {
                navController.navigate(R.id.action_profileFragment_to_parameters_Fragment)
            }
            // Добавьте другие случаи для открытия других фрагментов
        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}