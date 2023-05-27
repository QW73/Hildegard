package com.qw73.hildegard.screens.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.qw73.hildegard.R
import com.qw73.hildegard.base.BaseActivity
import com.qw73.hildegard.data.OPEN_MENU_SETTING
import com.qw73.hildegard.data.OPEN_ORDERS
import com.qw73.hildegard.data.OPEN_PARAMETERS
import com.qw73.hildegard.data.OPEN_PERSONAL_DATA
import com.qw73.hildegard.data.bd.Dish
import com.qw73.hildegard.data.bd.DishDao
import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.databinding.ActivityMainBinding
import com.qw73.hildegard.navigate.Navigator
import com.qw73.hildegard.screens.main.profile.ProfileViewModel
import com.qw73.hildegard.screens.main.profile.personalData.PersonalDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    @Inject
    lateinit var dishDao: DishDao

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

        CoroutineScope(Dispatchers.IO).launch {
            // Удаляем все записи перед добавлением новых
            dishDao.clearTable()
            dishDao.resetId()
            populateDatabase(dishDao)
            checkIfDishesInserted(dishDao)
        }
    }



    private suspend fun checkIfDishesInserted(dishDao: DishDao) {
        val dishes = dishDao.getDishes()
        if (dishes.isNotEmpty()) {
            // Записи были внесены в базу данных
            for (dish in dishes) {
                // Выводите информацию о каждой записи
                Log.d("MainActivity", "Dish ID: ${dish.id}")
                Log.d("MainActivity", "Category: ${dish.category}")
                Log.d("MainActivity", "Name: ${dish.name}")
                // и так далее...
            }
        } else {
            // Записи не были внесены в базу данных
            Log.d("databasev","ne vnesena")
        }
    }

    private suspend fun populateDatabase(dishDao: DishDao) {

        val dish1 = Dish(
            category = "Category 1",
            name = "Dish 1",
            image = "image1.jpg",
            calories = 100,
            proteins = 10,
            fats = 5,
            carbohydrates = 20,
            exclusions = listOf("Exclusion 1", "Exclusion 2"),
            ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3")
        )

        val dish2 = Dish(
            category = "Category 2",
            name = "Dish 2",
            image = "image2.jpg",
            calories = 200,
            proteins = 15,
            fats = 8,
            carbohydrates = 25,
            exclusions = listOf("Exclusion 3"),
            ingredients = listOf("Ingredient 4", "Ingredient 5", "Ingredient 6")
        )

        val dish3 = Dish(
            category = "Category 3",
            name = "Dish 3",
            image = "image3.jpg",
            calories = 150,
            proteins = 12,
            fats = 6,
            carbohydrates = 18,
            exclusions = listOf("Exclusion 5"),
            ingredients = listOf("Ingredient 7", "Ingredient 8")
        )

        val dish4 = Dish(
            category = "Category 3",
            name = "Dish 4",
            image = "image3.jpg",
            calories = 150,
            proteins = 12,
            fats = 6,
            carbohydrates = 18,
            exclusions = listOf("Exclusion 5"),
            ingredients = listOf("Ingredient 7", "Ingredient 8")
        )

        val dishes = listOf(dish1, dish2, dish3,dish4)
        dishDao.insertDishes(dishes)
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

            OPEN_MENU_SETTING -> {
                navController.navigate(R.id.action_profileFragment_to_menu_settings_Fragment)
            }

            OPEN_ORDERS -> {
               navController.navigate(R.id.action_profileFragment_to_ordersFragment)
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