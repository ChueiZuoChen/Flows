package com.example.flows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {
    private val _liveData = MutableLiveData<String>("Hello World!")
    val liveData: LiveData<String> = _liveData

    private val _stateFlow = MutableStateFlow<String>("Hello World!")
    val stateFlow = _stateFlow.asStateFlow()

    //for share flow it also can construct like stateflow, but it don't have to provide initial value,
    //becuase shared flow just as normal flow, it just keep value in itself
    //share normal use in a onetime event, like snake bar, toast...etc.
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(): Unit {
        _liveData.value = "Live Data"
    }

    fun triggerStateFlow(): Unit {
        _stateFlow.value = "State Flow"
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("ShareFlow")
        }
    }

    //Normal Flow
    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5){
                emit("Count: $it")
                delay(1000L)
            }
        }
    }






}