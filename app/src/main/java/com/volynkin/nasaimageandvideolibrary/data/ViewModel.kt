package com.volynkin.nasaimageandvideolibrary.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.volynkin.nasaimageandvideolibrary.networking.NetworkState
import com.volynkin.nasaimageandvideolibrary.paging.ItemDataSourceFactory


class ViewModel : ViewModel() {

    private val repository = Repository
    private val urlLiveData = MutableLiveData<String>()
    private val dataSource = ItemDataSourceFactory()
    private val listLivedPaged = LivePagedListBuilder(dataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState> = switchMap(dataSource.source) { it.getNetworkState() }

    val url: LiveData<String>
        get() = urlLiveData

    val listPaged:  LiveData<PagedList<NASAItem>>
        get() = listLivedPaged


    fun fetch(text: String) {
        val search = text.trim()
        dataSource.updateText(search)
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .build()

    private val isLoadingLiveData = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    fun searchUrl(type: String, json: String) {
        isLoadingLiveData.postValue(true)
        repository.searchItem(
            type,
            json,
            onComplete = { url ->
                isLoadingLiveData.postValue(false)
                urlLiveData.postValue(url)
            }
        )
    }
}