/*
  Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

  Licensed under the Apache License, Version 2.0 (the "License").
  You may not use this file except in compliance with the License.
  A copy of the License is located at

      http://www.apache.org/licenses/LICENSE-2.0

  or in the "license" file accompanying this file. This file is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
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