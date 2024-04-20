package com.dac.tictactoe.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope

open class BaseViewModel : ViewModel(), CoroutineScope {

    private val _apiError: MutableLiveData<String> = MutableLiveData()
    val apiError: LiveData<String> get() = _apiError


    private val _apiLoading: MutableLiveData<Boolean> = MutableLiveData()
    val apiLoading: LiveData<Boolean> get() = _apiLoading


    private val jobMap = HashMap<String, Job>()

    final override val coroutineContext = Dispatchers.IO

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _apiError.postValue(throwable.message ?: throwable.localizedMessage)
    }


    init {
        // Attach the exception handler to the coroutine context
        coroutineContext + coroutineExceptionHandler
    }

    private fun addJob(key: String, job: Job) {
        jobMap[key]?.cancel() // Cancel the previous job if it exists for this key
        jobMap[key] = job
    }

    private fun showError(errorMessage: String) {
        _apiError.postValue(errorMessage)
    }

    private fun showHideDialog(isShow: Boolean) {
        _apiLoading.postValue(isShow)
    }

    override fun onCleared() {
        super.onCleared()
        jobMap.forEach { it.value.cancel() }
    }


}