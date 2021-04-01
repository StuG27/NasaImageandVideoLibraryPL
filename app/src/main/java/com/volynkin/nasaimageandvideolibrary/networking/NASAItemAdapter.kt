package com.volynkin.nasaimageandvideolibrary.networking


import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import com.volynkin.nasaimageandvideolibrary.data.NASAItem
import com.volynkin.nasaimageandvideolibrary.data.NASAList


class NASAItemAdapter {

    @FromJson
    fun fromJson(NASACollection: NASACollection): NASAList {
        val items: MutableList<NASAItem> = mutableListOf()
        NASACollection.collection.items.forEach { item ->
            items.add(
                NASAItem(
                    item.data[0].id,
                    item.href,
                    item.links[0].preview,
                    item.data[0].description ?: "",
                    item.data[0].title,
                    item.data[0].date,
                    item.data[0].type,
                    item.data[0].keywords?.joinToString(" ") ?: ""
                )
            )
        }
        return NASAList(
            NASACollection.collection.metadata.totalHits,
            items
        )
    }

    @ToJson
    fun toJson(list: NASAList): NASACollection {
        val items: MutableList<Item> = mutableListOf()
        list.items.forEach { item ->
            items.add(
                Item(
                    listOf(Link(item.preview)),
                    item.jsonLink,
                    listOf(
                        Data(
                            item.id,
                            item.description,
                            item.title,
                            item.date,
                            item.type,
                            item.keywords.split(" ")
                        )
                    )
                )
            )
        }
        val rawItem = RawItem(
            items,
            Metadata(list.size)
        )
        return NASACollection(
            rawItem
        )
    }

    @JsonClass(generateAdapter = true)
    data class NASACollection(
        val collection: RawItem
    )

    @JsonClass(generateAdapter = true)
    data class RawItem(
        val items: List<Item>,
        val metadata: Metadata
    )

    @JsonClass(generateAdapter = true)
    data class Metadata(
        @Json(name = "total_hits")
        val totalHits: String
    )

    @JsonClass(generateAdapter = true)
    data class Item(
        val links: List<Link>,
        val href: String,
        val data: List<Data>
    )

    @JsonClass(generateAdapter = true)
    data class Link(
        @Json(name = "href")
        val preview: String
    )

    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "nasa_id")
        val id: String,
        val description: String?,
        val title: String,
        @Json(name = "date_created")
        val date: String,
        @Json(name = "media_type")
        val type: String,
        val keywords: List<String>?
    )
}