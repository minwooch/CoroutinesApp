package com.applsh.coroutinesapp.ui.some

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class SomeViewModel @Inject constructor(

) : ViewModel() {

    private val r = Random(0)

    val loadingState = MutableStateFlow<LoadingState>(LoadingState.NotLoading)

    val dataStateOutput = loadingState
        // .filter { false }
        .transform {
            if (it != LoadingState.Loading) return@transform
            try {
                val data = heavyWork()
                loadingState.value = LoadingState.NotLoading
                emit(data)
            } catch (e: Exception) {
                loadingState.value = LoadingState.Error
            }
        }
        .stateIn(viewModelScope + Dispatchers.IO, SharingStarted.Lazily, null)

    val dataEventOutput = loadingState
        .filter { false }
        .transform {
            if (it != LoadingState.Loading) return@transform
            try {
                val data = heavyWork()
                loadingState.value = LoadingState.NotLoading
                emit(data)
            } catch (e: Exception) {
                loadingState.value = LoadingState.Error
            }
        }.flowOn(Dispatchers.IO)

    fun onClick() {
        viewModelScope.launch {
            repeat(100) {
                launch(Dispatchers.IO) {
                    loadDataState()
                }
            }
        }
    }

    private suspend fun loadDataState() {
        loadingState.value = LoadingState.Loading
    }

    private suspend fun loadDataEvent() {
        loadingState.value = LoadingState.Loading
    }

    private suspend fun heavyWork(): Int {
        delay(1000L)
        val exception = r.nextBoolean()
        if (exception) {
            throw Exception("")
        }
        return r.nextInt()
    }
}

sealed class LoadingState {
    object NotLoading : LoadingState()
    object Loading : LoadingState()
    object Error : LoadingState()
}