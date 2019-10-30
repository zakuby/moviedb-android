package org.themoviedb.screens.movie.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.core.network.response.ErrorResponse
import javax.inject.Inject

class MoviesViewModel @Inject constructor() : BaseViewModel(){

    val isResponseError = ObservableBoolean(false)

    private val errorResponse = MutableLiveData<ErrorResponse>()

    fun getErrorResponse(): LiveData<ErrorResponse> = errorResponse
}