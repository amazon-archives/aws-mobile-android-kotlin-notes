/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazonaws.mobile.samples.awsomenotes.ui

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.models.Note

/**
 * The list adapter is an integral part of the RecyclerView, providing a mechanism to do per item
 * layout and paging.  This particular version is a PagedListAdpater from the Android Architecture
 * Components paging library that provides a mechanism for loading data a page at a time.
 */
class NoteListAdapter(private val onClick: (Note) -> Unit): PagedListAdapter<Note, NoteListViewHolder>(NOTE_COMPARATOR) {
    companion object {
        private val NOTE_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note?, newItem: Note?): Boolean = oldItem?.noteId == newItem?.noteId
            override fun areContentsTheSame(oldItem: Note?, newItem: Note?): Boolean = oldItem?.equals(newItem)
                    ?: false
        }
    }

    /**
     * Called when a viewholder needs to be bound to actual data.  The viewholder must exist
     * first (it's created using onCreateViewHolder())
     */
    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.note = getItem(position)
        holder.itemView.setOnClickListener { onClick(holder.note!!) }
    }

    /**
     * Called when a new item needs to be added to the list.  It will inflate the layout,
     * returning a viewholder that is bound to the new layout.  Once data is loaded, the
     * viewholder will then be bound to the data via onBindViewHolder()
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder
        = NoteListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_content, parent, false))
}