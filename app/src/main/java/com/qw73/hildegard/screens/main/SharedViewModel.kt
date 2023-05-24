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

    fun saveOneMore(oneMore: Boolean) {
        savedOneMore= oneMore
    }

    fun getOneMore(): Boolean? {
        return savedOneMore
    }

    fun saveIsDataFull(isDataFull: Boolean) {
        savedIsDataFull= isDataFull
        Log.d("datafull",isDataFull.toString())
    }

    fun getIsDataFull(): Boolean? {
        return savedIsDataFull
    }

    fun saveWeight(weight: String) {
        savedWeight= weight
    }

    fun getSavedWeight(): String? {
        return savedWeight
    }

    fun saveAge(age: String) {
        savedAge= age
    }

    fun getSavedAge(): String? {
        return savedAge
    }

    fun saveHeight(height: String) {
        savedHeight= height
    }

    fun getSavedHeight(): String? {
        return savedHeight
    }

    fun saveGoals(goals: String) {
        savedGoals= goals
    }

    fun getSavedGoals(): String? {
        return savedGoals
    }

    fun saveActivity(activity: String) {
        savedActivity= activity
    }

    fun getSavedActivity(): String? {
        return savedActivity
    }

    fun saveGender(gender: String) {
        savedGender = gender
    }

    fun getSavedGender(): String? {
        return savedGender
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

    fun getSavedEmail(): String? {
        return savedEmail
    }

    fun getSavedBirthday(): String? {
        return savedBirthday
    }

    // добавить когда дата полная то и вычисление тутже
}