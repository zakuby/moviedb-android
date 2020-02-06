package org.themoviedb.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.themoviedb.ui.base.BaseViewModel
import org.themoviedb.data.local.models.Profile
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val profile = MutableLiveData<Profile>()

    init { profile.postValue(Profile()) }

    fun getProfile(): LiveData<Profile> = profile
}