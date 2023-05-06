package com.qure.home

import com.qure.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : BaseViewModel() {
    private val _fragment :MutableStateFlow<Int> = MutableStateFlow(0)
    val fragment : StateFlow<Int>
        get() = _fragment

    fun setCurrentFragment(currentFragment: Int) {
        _fragment.value = currentFragment
    }
}