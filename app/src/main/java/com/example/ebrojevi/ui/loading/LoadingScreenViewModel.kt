package com.example.ebrojevi.ui.loading

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.net.URLDecoder

class LoadingScreenViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val rawString: String =
        savedStateHandle["queryString"] ?: throw IllegalArgumentException("Missing query string")
    val queryString: String = URLDecoder.decode(rawString, "UTF-8")

    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(LoadingScreenState())
    val state = _state
        .onStart {
            Log.d("EStringLogger", "LoadingScreenViewModel started with queryString: $queryString")
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoadingScreenState()
        )

    fun onAction(action: LoadingScreenAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}