package com.xfinity.features.masterdetail.data.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xfinity.R
import com.xfinity.data.model.response.RelatedTopic
import com.xfinity.databinding.ItemDetailBinding
import com.xfinity.features.masterdetail.CharacterViewModel

class ItemListAdapter : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {
    private var isSearching: Boolean = false
    private var filteredItems: MutableList<RelatedTopic> = ArrayList()
    private var items: List<RelatedTopic> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: RelatedTopic = if (isSearching) {
            filteredItems[position]
        } else {
            items[position]
        }
        holder.bind(item)
    }

    fun filter(query: CharSequence) {
        if (filteredItems.isNotEmpty()) {
            filteredItems.clear()
        }
        if (items.isNotEmpty()) {
            isSearching = true
            for (result in items) {
                if (result.text!!.contains(query.toString())) {
                    filteredItems.add(result)
                }
            }
        } else {
            isSearching = false
        }
        notifyDataSetChanged()
    }

    fun closeSearch() {
        isSearching = false
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (isSearching) {
            filteredItems.size
        } else {
            items.size
        }
    }

    fun updateCharacterList(items: List<RelatedTopic>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        private val characterViewModel = CharacterViewModel()

        fun bind(character: RelatedTopic) {
            characterViewModel.bind(character)
            binding.viewModel = characterViewModel
        }
    }
}