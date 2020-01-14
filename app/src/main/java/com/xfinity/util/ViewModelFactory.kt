package com.xfinity.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.xfinity.data.DataManager

import com.xfinity.features.masterdetail.CharacterListViewModel

import javax.inject.Inject

class ViewModelFactory @Inject
constructor(private val dataManager: DataManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == CharacterListViewModel::class.java) return CharacterListViewModel(dataManager) as T
        throw RuntimeException("Cannot create an instance of $modelClass", ClassNotFoundException("Class not supported in ViewModelFactory"))
    }
}