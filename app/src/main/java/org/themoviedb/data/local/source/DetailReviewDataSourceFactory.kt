package org.themoviedb.data.local.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.themoviedb.data.local.models.Review

class DetailReviewDataSourceFactory constructor(
    private val dataSource: DetailReviewDataSource
) : DataSource.Factory<Int, Review>() {

    private val dataSourceLiveData = MutableLiveData<DetailReviewDataSource>()

    override fun create(): DataSource<Int, Review> {
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun getDataSource(): LiveData<DetailReviewDataSource> = dataSourceLiveData
}