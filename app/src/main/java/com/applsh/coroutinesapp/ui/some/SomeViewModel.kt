package com.applsh.coroutinesapp.ui.some

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class SomeViewModel @Inject constructor(

) : ViewModel() {

    private val r = Random(0)

    val loadingState = MutableStateFlow(false)

    private val _dataStateOutput = MutableStateFlow<Int?>(null)
    val dataStateOutput: Flow<Int?> = _dataStateOutput

    private val _dataEventOutput = Channel<Int>(0)
    val dataEventOutput = _dataEventOutput.consumeAsFlow()

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
        if (loadingState.compareAndSet(expect = false, update = true)) {
            try {
                _dataStateOutput.value = heavyWork()
            } finally {
                loadingState.value = false
            }
        }
    }

    private suspend fun loadDataEvent() {
        if (loadingState.compareAndSet(expect = false, update = true)) {
            try {
                _dataEventOutput.send(heavyWork())
            } finally {
                loadingState.value = false
            }
        }
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