package com.volynkin.nasaimageandvideolibrary.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.volynkin.nasaimageandvideolibrary.data.NASAItem
import com.volynkin.nasaimageandvideolibrary.data.NASAList
import com.volynkin.nasaimageandvideolibrary.data.Repository
import com.volynkin.nasaimageandvideolibrary.networking.NetworkState

class ItemDataSource(
    private val text: String
) : PageKeyedDataSource<Int, NASAItem>() {

    private val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NASAItem>
    ) {
        find(1) {
            callback.onResult(it.items, null, 2)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NASAItem>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NASAItem>) {
        val page = params.key
        find(page) {
            callback.onResult(it.items, page + 1)
        }
    }

    private fun find(
        page: Int,
        callback: (NASAList) -> Unit
    ) {
        networkState.postValue(NetworkState.RUNNING)
        Repository.checkPreferences(page, text, callback)
        networkState.postValue(NetworkState.SUCCESS)
    }

    fun getNetworkState(): LiveData<NetworkState> =
        networkState

    fun refresh() =
        this.invalidate()
}