package com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.R
import com.github.jan222ik.jetpacklivedatapaginationwithroomfromvmexample.Word
import kotlinx.android.synthetic.main.fragment_item.view.*

class PagedAdapter : PagedListAdapter<Word, PagedAdapter.MyViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bindPost(it) }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val idView: TextView = view.item_number
        private val contentView: TextView = view.content

        fun bindPost(word: Word) {
            idView.text = word.freq.toString()
            contentView.text = word.word
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.freq == newItem.freq && oldItem.word == newItem.word
        }

    }
}