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
package com.amazonaws.mobile.samples.awsomenotes.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.amazonaws.mobile.samples.awsomenotes.services.DataService
import com.amazonaws.mobile.samples.awsomenotes.models.Note

class NotesRepository(dataService: DataService) {
    /**
     * An observable lifecycle-aware version of a paged list of notes.  This is used
     * to render a RecyclerView of all the notes.
     */
    val pagedList: LiveData<PagedList<Note>>

    /**
     * A link to the data source that generates the paged list.
     */
    private val dataSource: LiveData<NotesDataSource>

    init {
        val sourceFactory = NotesDataSourceFactory(dataService)
        dataSource = sourceFactory.currentDataSource
        pagedList = LivePagedListBuilder(sourceFactory, 20).build()
    }

    /**
     * API operation to save an item to the data store
     */
    fun save(note: Note, callback: () -> Unit) = dataSource.value!!.saveItem(note, callback)

    /**
     * API operation to delete an item from the data store
     */
    fun delete(noteId: String, callback: () -> Unit) = dataSource.value!!.deleteItem(noteId, callback)

    /**
     * API operation to obtain an item from the data store
     */
    fun get(noteId: String, callback: (Note?) -> Unit) = dataSource.value!!.getItem(noteId, callback)
}