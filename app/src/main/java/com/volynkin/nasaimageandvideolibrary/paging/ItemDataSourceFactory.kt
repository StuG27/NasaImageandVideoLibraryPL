package com.volynkin.nasaimageandvideolibrary.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.volynkin.nasaimageandvideolibrary.data.NASAItem

class ItemDataSourceFactory(private var text: String = "initial") :
    DataSource.Factory<Int, NASAItem>() {

    val source = MutableLiveData<ItemDataSource>()

    override fun create(): DataSource<Int, NASAItem> {
        val source = ItemDataSource(text)
        this.source.postValue(source)
        return source
    }

    private fun getSource() = source.value

    fun updateText(text: String) {
        this.text = text
        getSource()?.refresh()
    }
}