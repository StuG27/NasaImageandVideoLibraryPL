package com.volynkin.nasaimageandvideolibrary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volynkin.nasaimageandvideolibrary.R
import com.volynkin.nasaimageandvideolibrary.data.NASAItem
import com.volynkin.nasaimageandvideolibrary.databinding.ItemBinding


class AdapterPaged(
    private val onItemClick: (title: String, description: String, link: String, type: String) -> Unit,
) : PagedListAdapter<NASAItem, RecyclerView.ViewHolder>(
    diffCallback
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(layoutInflater, parent, false)
        return MainHolder.ItemHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MainHolder.ItemHolder -> holder.bind(getItem(position))
        }
    }

    abstract class MainHolder(
        binding: ItemBinding,
        onItemClick: (title: String, description: String, link: String, type: String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var title: String? = null
        private var description: String? = null
        private var jsonLink: String? = null
        private var type: String? = null

        init {
            binding.root.setOnClickListener {
                onItemClick(
                    title ?: "", description ?: "",
                    jsonLink ?: "", type ?: ""
                )
            }
        }

        private val iVPreview = binding.iVPreview
        private val tVTitle = binding.tVTitle
        private val tVDate = binding.tVDate
        private val tVKeywords = binding.tVKeywords
        private val tVType = binding.tVType

        protected fun bindMainInfo(
            jsonLink: String,
            preview: String,
            description: String,
            title: String,
            date: String,
            type: String,
            keywords: String
        ) {
            this.title = title
            this.description = description
            this.jsonLink = jsonLink
            this.type = type
            if (title.length > 50) {
                tVTitle.text = title.substring(0, 50).plus("...")
            } else {
                tVTitle.text = title
            }
            Glide
                .with(itemView)
                .load(preview)
                .placeholder(R.drawable.ic_twotone_photo_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(iVPreview)
            tVDate.text = date.substring(0, 10)
            if (keywords.isEmpty()) {
                tVKeywords.text = ""
            } else {
                tVKeywords.text = keywords
            }
            tVType.text = type
        }

        class ItemHolder(
            binding: ItemBinding,
            onItemClick: (title: String, description: String, link: String, type: String) -> Unit,
        ) : MainHolder(binding, onItemClick) {

            fun bind(item: NASAItem?) {
                bindMainInfo(
                    item!!.jsonLink, item.preview, item.description,
                    item.title, item.date, item.type, item.keywords
                )
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<NASAItem>() {
            override fun areItemsTheSame(oldItem: NASAItem, newItem: NASAItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NASAItem, newItem: NASAItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
