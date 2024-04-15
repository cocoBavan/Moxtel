package com.bltech.moxtel.global

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor() : ViewModel(), TitleSetter {
    private val _titleFlow = MutableStateFlow("Home")
    val titleFlow = _titleFlow.asStateFlow()
    override fun setTitle(title: String) {
        _titleFlow.value = title
    }
}


interface TitleSetter {
    fun setTitle(title: String)
}
