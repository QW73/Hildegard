package com.qw73.hildegard.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private var savedName: String? = null
    private var savedEmail: String? = null
    private var savedBirthday: String? = null

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private var savedGender: String? = null
    private var savedActivity: String? = null
    private var savedGoals: String? = null
    private var savedAge: String? = null
    private var savedHeight: String? = null
    private var savedWeight: String? = null

    private var savedIsDataFull: Boolean? = null
    private var savedOneMore: Boolean? = null

    private var savedCalories: Int? = null
    private var savedProtein: Int? = null
    private var savedCarbohydrates: Int? = null
    private var savedFat: Int? = null

    private var savedMilk: Boolean? = null
    private var savedNuts: Boolean? = null
    private var savedSugar: Boolean? = null
    private var savedGluten: Boolean? = null
    private var savedEggs: Boolean? = null


    fun saveEggs(eggs: Boolean) {
        savedEggs= eggs
    }
    fun saveSugar(sugar: Boolean) {
        savedSugar= sugar
    }

    fun saveGluten(gluten: Boolean) {
        savedGluten= gluten
    }

    fun saveMilk(milk: Boolean) {
        savedMilk = milk
    }

    fun saveNuts(nuts: Boolean) {
        savedNuts= nuts
    }

    fun saveCalories(calories: Int) {
        savedCalories = calories
    }

    fun saveProtein(protein: Int) {
        savedProtein = protein
    }

    fun saveCarbohydrates(carbohydrates: Int) {
        savedCarbohydrates = carbohydrates
    }

    fun saveFat(fat: Int) {
        savedFat = fat
    }

    fun saveOneMore(oneMore: Boolean) {
        savedOneMore = oneMore
    }

    fun saveIsDataFull(isDataFull: Boolean) {
        savedIsDataFull = isDataFull
        Log.d("datafull", isDataFull.toString())
    }

    fun saveWeight(weight: String) {
        savedWeight = weight
    }

    fun getSavedWeight(): String? {
        return savedWeight
    }

    fun saveAge(age: String) {
        savedAge = age
    }

    fun saveHeight(height: String) {
        savedHeight = height
    }

    fun saveGoals(goals: String) {
        savedGoals = goals
    }

    fun saveActivity(activity: String) {
        savedActivity = activity
    }

    fun saveGender(gender: String) {
        savedGender = gender
    }

    fun saveName(name: String) {
        savedName = name
    }

    fun saveEmail(email: String) {
        savedEmail = email
    }

    fun saveBirthday(birthday: String) {
        savedBirthday = birthday
    }

    fun setSavedName(name: String) {
        _name.value = name
    }

    fun getSavedName(): String? {
        return savedName
    }

    fun getSavedGoals(): String? {
        return savedGoals
    }

    fun getSavedActivity(): String? {
        return savedActivity
    }

    fun getSavedAge(): String? {
        return savedAge
    }

    fun getSavedGender(): String? {
        return savedGender
    }

    fun getSavedEmail(): String? {
        return savedEmail
    }

    fun getSavedHeight(): String? {
        return savedHeight
    }

    fun getSavedBirthday(): String? {
        return savedBirthday
    }

    fun getIsDataFull(): Boolean? {
        return savedIsDataFull
    }

    fun getCalories(): Int? {
        return savedCalories
    }

    fun getProtein(): Int? {
        return savedProtein
    }

    fun getCarbohydrates(): Int? {
        return savedCarbohydrates
    }

    fun getFat(): Int? {
        return savedFat
    }

    fun getOneMore(): Boolean? {
        return savedOneMore
    }

    fun getGluten(): Boolean? {
        return savedGluten
    }

    fun getEggs(): Boolean? {
        return savedEggs
    }

    fun getMilk(): Boolean? {
        return savedMilk
    }

    fun getNuts(): Boolean? {
        return savedNuts
    }

    fun getSugar(): Boolean? {
        return savedSugar
    }

    // добавить когда дата полная то и вычисление тутже
    fun calculateDiet() {
        if (savedIsDataFull == true) {

            var calories = 0 // калории
            var protein = 0 // белки
            var carbohydrates = 0 // углеводы
            var fat = 0 //жиры

            var activity =
                when (savedActivity) {
                    "level1" -> 1.2
                    "level2" -> 1.375
                    "level3" -> 1.55
                    "level4" -> 1.725
                    else -> {
                        0.00
                    }
                }


            if (savedGoals == "goals1") {
                calories = dailyCalories(activity)
                protein = dailyProtein(activity, 1)
                carbohydrates = (calories * 0.5 / 4).toInt()
                fat = (calories * 0.25 / 9).toInt()
            } else if (savedGoals == "goals2") {
                calories = ((dailyCalories(activity) * 0.85).toInt())
                protein = dailyProtein(activity, 2)
                carbohydrates = (calories * 0.45 / 4).toInt()
                fat = (calories * 0.25 / 9).toInt()
            } else if (savedGoals == "goals3") {
                calories = ((dailyCalories(activity) * 1.15).toInt())
                protein = dailyProtein(activity, 3)
                carbohydrates = (calories * 0.55 / 4).toInt()
                fat = (calories * 0.25 / 9).toInt()
            }

            saveCalories(calories)
            saveProtein(protein)
            saveCarbohydrates(carbohydrates)
            saveFat(fat)

            Log.d("dailyC", calories.toString())
            Log.d("dailyP", protein.toString())
            Log.d("dailyCar", carbohydrates.toString())
            Log.d("dailyF", fat.toString())

        }
    }


    private fun dailyCalories(level: Double): Int {
        val calories = if (savedGender == "Male") {
            ((66 + 13.75 * savedWeight!!.toInt() + 5 * savedHeight!!.toInt() - 6.75 * savedAge!!.toInt()) * level).toInt()
        } else {
            ((655 + 9.56 * savedWeight!!.toInt() + 1.85 * savedHeight!!.toInt() - 4.68 * savedAge!!.toInt()) * level).toInt()
        }
        return calories
    }

    private fun dailyProtein(level: Double, goal: Int): Int {
        val protein: Int = when (goal) {
            1 -> {
                (savedWeight!!.toInt() * 0.8).toInt()
            }
            2 -> {
                (savedWeight!!.toInt() * 1.2).toInt()
            }
            3 -> (savedWeight!!.toInt() * 1)
            else -> {
                0
            }
        }
        return protein
    }
}