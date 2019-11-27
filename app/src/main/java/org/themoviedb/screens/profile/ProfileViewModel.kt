package org.themoviedb.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.themoviedb.core.base.BaseViewModel
import org.themoviedb.data.models.Profile
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val profile = MutableLiveData<Profile>()

    init { profile.postValue(Profile()) }

    fun getProfile(): LiveData<Profile> = profile
}