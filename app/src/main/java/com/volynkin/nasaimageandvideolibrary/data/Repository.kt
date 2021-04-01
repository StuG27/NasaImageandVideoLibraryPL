package com.volynkin.nasaimageandvideolibrary.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.volynkin.nasaimageandvideolibrary.extensions.AppPreferences
import com.volynkin.nasaimageandvideolibrary.networking.NASAItemAdapter
import com.volynkin.nasaimageandvideolibrary.networking.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object Repository {

    private val errorList: NASAList = NASAList("error", mutableListOf())
    private val errorListString: List<String> = listOf("error")

    fun searchItem(
        type: String,
        json: String,
        onComplete: (String) -> Unit
    ) {
        Network.api.downloadItemInfo(json).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val item = response.body() ?: ""
                        val tempList = parseElement(item)
                        val result: MutableList<String> = mutableListOf()
                        tempList.forEach {
                            if (type == "image") {
                                if (it.last() == 'g') {
                                    result.add(it)
                                }
                            } else {
                                if (it.contains("small.mp4", true)) {
                                    result.add(it)
                                }
                                if (result.size == 0) {
                                    if (it.contains(".mp4", true)) {
                                        result.add(it)
                                    }
                                }
                            }
                        }
                        onComplete(result[0])
                    } else {
                        onComplete("error")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    onComplete("error")
                }
            }
        )
    }

    fun checkPreferences(
        page: Int,
        text: String,
        onComplete: (NASAList) -> Unit,
    ) {
        if (text == "initial") {
            val json = AppPreferences.json ?: " "
            if (json == " ") {
                searchList(page, " ", onComplete)
            } else {
                AppPreferences.json = json
                val tempList = parse(json)
                onComplete(tempList)
            }
        } else {
            searchList(page, text, onComplete)
        }
    }

    private fun searchList(
        page: Int,
        text: String,
        onComplete: (NASAList) -> Unit
    ) {
        Network.api.downloadList(page, text).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val json = response.body() ?: " "
                        AppPreferences.json = json
                        val tempList = parse(json)
                        onComplete(tempList)
                    } else {
                        onComplete(errorList)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    onComplete(errorList)
                }
            }
        )
    }

    fun parse(responseBodyString: String): NASAList {
        val moshi = Moshi.Builder().add(NASAItemAdapter()).build()
        val adapter = moshi.adapter(NASAList::class.java).nonNull()
        return try {
            adapter.fromJson(responseBodyString) ?: errorList
        } catch (e: Exception) {
            errorList
        }
    }

    private fun parseElement(responseBodyString: String): List<String> {
        val moshi = Moshi.Builder().build()
        val stringListType = Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        val adapter = moshi.adapter<List<String>>(stringListType).nonNull()
        return try {
            adapter.fromJson(responseBodyString) ?: errorListString
        } catch (e: Exception) {
            errorListString
        }
    }
}
