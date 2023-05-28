package com.qw73.hildegard.screens.main

import android.net.Uri
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.viewModels
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Проверьте, является ли текущий фрагмент вашим фрагментом, в котором нужно скрыть ActionBar
            if (destination.id == R.id.home_fragment) {
                // Скрыть ActionBar в фрагменте HomeFragment
                supportActionBar?.hide()
            } else {
                // Показать ActionBar для других фрагментов
                supportActionBar?.show()
            }
        }
        setupSmoothBottomMenu()
        bindViews()
        bindObservers()

        CoroutineScope(Dispatchers.IO).launch {
            // Удаляем все записи перед добавлением новых
            dishDao.clearTable()
            dishDao.resetId()
            populateDatabase(dishDao)
        }
    }

    private suspend fun populateDatabase(dishDao: DishDao) {

        val dish1 = Dish(
            category = "Завтрак",
            name = "Сырники с клубничным кули",
            image = Uri.parse("file:///android_asset/Сырники с клубничным кули.png"),
            price = 269,
            grams = 240,
            calories = 371,
            proteins = 21,
            fats = 12,
            carbohydrates = 44,
            exclusions = listOf("Milk","Sugar"),
            ingredients = listOf("Сырники",
                "Клубничное кули",
                "Клубника",
                "Киви",
                "Ванильный соус",
                "Мята")
        )

        val dish2 = Dish(
            category = "Завтрак",
            name = "Кабачковые оладьи",
            image = Uri.parse("file:///android_asset/Кабачковые оладьи.png"),
            price = 269,
            grams = 230,
            calories = 254,
            proteins = 6,
            fats = 17,
            carbohydrates = 20,
            exclusions = listOf("Milk"),
            ingredients = listOf("Оладьи из кабачка", "Томаты черри", "Микс салата", "Сметана")
        )

        val dish3 = Dish(
            category = "Завтрак",
            name = "Шакшука",
            image = Uri.parse("file:///android_asset/Шакшука.png"),
            price = 259,
            grams = 270,
            calories = 161,
            proteins = 12,
            fats = 10,
            carbohydrates = 7,
            exclusions = listOf("Eggs"),
            ingredients = listOf("Яйцо куриное",
                "Томаты",
                "Болгарский перец",
                "Лук",
                "Чеснок",
                "Кинза")
        )

        val dish4 = Dish(
            category = "Завтрак",
            name = "Киш с курицей и брокколи",
            image = Uri.parse("file:///android_asset/Киш с курицей и брокколи.png"),
            price = 199,
            grams = 110,
            calories = 259,
            proteins = 7,
            fats = 19,
            carbohydrates = 25,
            exclusions = listOf("Eggs","Gluten", "Milk"),
            ingredients = listOf("Песочное тесто",
                "Куриное бедро",
                "Брокколи",
                "Сливки",
                "Молоко",
                "Куриное яйцо",
                "Сыр Гауда",
                "Микс салата")
        )

        val dish5 = Dish(
            category = "Завтрак",
            name = "Овсяная каша на банановом молоке",
            image = Uri.parse("file:///android_asset/Овсяная каша на банановом молоке.png"),
            price = 199,
            grams = 200,
            calories = 153,
            proteins = 6,
            fats = 5,
            carbohydrates = 22,
            exclusions = listOf("Gluten","Sugar"),
            ingredients = listOf("Овсяные хлопья", "Банановое молоко", "Масло", "Сахар")
        )

        val dish6 = Dish(
            category = "Завтрак",
            name = "Гречневая каша с грибами",
            image = Uri.parse("file:///android_asset/Гречневая каша с грибами.png"),
            price = 169,
            grams = 180,
            calories = 287,
            proteins = 13,
            fats = 3,
            carbohydrates = 52,
            exclusions = listOf("Gluten"),
            ingredients = listOf("Гречневая крупа", "Шампиньоны", "Лук", "Растительное масло")
        )

        val dish7 = Dish(
            category = "Завтрак",
            name = "Блинчики с картофелем, грибами и беконом",
            image = Uri.parse("file:///android_asset/Блинчики с картофелем, грибами и беконом.png"),
            price = 258,
            grams = 260,
            calories = 387,
            proteins = 12,
            fats = 23,
            carbohydrates = 34,
            exclusions = listOf("Eggs","Gluten","Milk"),
            ingredients = listOf("Блинчики",
                "Картофель",
                "Шампиньоны",
                "Бекон",
                "Сметана",
                "Жареный лук",
                "Зелёный лук")
        )

        val dish8 = Dish(
            category = "Завтрак",
            name = "Сэндвич с лососем и томатами в тортилье",
            image = Uri.parse("file:///android_asset/Сэндвич с лососем и томатами в тортилье.png"),
            price = 319,
            grams = 240,
            calories = 407,
            proteins = 12,
            fats = 30,
            carbohydrates = 22,
            exclusions = listOf("Gluten","Milk"),
            ingredients = listOf("Пшеничная тортилья",
                "Томаты",
                "Сметана",
                "Маринованный лосось",
                "Салат-латук",
                "Сырный соус",
                "Томатный соус",
                "Зеленый лук")
        )

        val dish9 = Dish(
            category = "Завтрак",
            name = "Сырники со сметаной и цитрусовым медом",
            image = Uri.parse("file:///android_asset/Сырники со сметаной и цитрусовым медом.png"),
            price = 269,
            grams = 230,
            calories = 462,
            proteins = 22,
            fats = 22,
            carbohydrates = 45,
            exclusions = listOf("Eggs","Gluten"),
            ingredients = listOf("Творог",
                "Сахар",
                "Куриное яйцо",
                "Мука",
                "Сметана",
                "Цитрусовый мед",
                "Клубника",
                "Мята")
        )

        val dish10 = Dish(
            category = "Завтрак",
            name = "Русский завтрак",
            image = Uri.parse("file:///android_asset/Русский завтрак.png"),
            price = 359,
            grams = 230,
            calories = 541,
            proteins = 26,
            fats = 42,
            carbohydrates = 15,
            exclusions = listOf("Eggs","Milk"),
            ingredients = listOf("Оливье с говядиной и курицей",
                "Блинчик с мясной начинкой из курицы и свинины",
                "Куриное яйцо",
                "Сосиски",
                "Зелёный лук",
                "Сметана")
        )

        val dish11 = Dish(
            category = "Салат",
            name = "Салат «Цезарь» с курицей",
            image = Uri.parse("file:///android_asset/Салат «Цезарь» с курицей.png"),
            price = 289,
            grams = 185,
            calories = 418,
            proteins = 16,
            fats = 34,
            carbohydrates = 12,
            exclusions = listOf(),
            ingredients = listOf("Куриное филе",
                "Салат-латук",
                "Чесночные гренки",
                "Пармезан",
                "Соус «Цезарь»",
                "Томаты черри")
        )

        val dish12 = Dish(
            category = "Салат",
            name = "Салат «Греческий»",
            image = Uri.parse("file:///android_asset/Салат «Греческий».png"),
            price = 289,
            grams = 210,
            calories = 286,
            proteins = 7,
            fats = 25,
            carbohydrates = 8,
            exclusions = listOf(),
            ingredients = listOf("Салат-латук",
                "Болгарский перец",
                "Красный лук",
                "Оливки",
                "Огурец",
                "Томаты",
                "Сыр «Фета»",
                "Майоран",
                "Бальзамический уксус",
                "Оливковое масло",
                "Салат «Лолло Россо»")
        )

        val dish13 = Dish(
            category = "Салат",
            name = "Салат «Бора-Бора» с манго",
            image = Uri.parse("file:///android_asset/Салат «Бора-Бора» с манго.png"),
            price = 449,
            grams = 185,
            calories = 351,
            proteins = 10,
            fats = 30,
            carbohydrates = 12,
            exclusions = listOf(),
            ingredients = listOf("Креветки",
                "Авокадо",
                "Огурец",
                "Манго",
                "Микс салата",
                "Кунжут",
                "Лаймовая заправка")
        )
        val dishes = listOf(
            dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9, dish10,
            dish11, dish12, dish13,
        )
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