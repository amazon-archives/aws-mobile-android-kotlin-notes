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

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.amazonaws.mobile.samples.awsomenotes.models.Note
import com.amazonaws.mobile.samples.awsomenotes.services.DataService
import com.amazonaws.mobile.samples.awsomenotes.TAG

/**
 * A DataSource implements a paging system for a RecyclerView.  This one uses the DataService
 * to provide a paged view into the Notes API.
 */
class NotesDataSource(private val dataService: DataService) : PageKeyedDataSource<String, Note>() {
    /**
     * Part of the PageKeyedDataSource - load the first page of a list.  This is called when
     * the data source is first created or invalidated and assumes no nextToken has been
     * provided.
     */
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Note>) {
        Log.d(TAG, "loadInitial(${params.requestedLoadSize})")
        dataService.loadNotes(params.requestedLoadSize, null) { response ->
            Log.d(TAG, "loadItems response = (${response.items.size}, \"${response.nextToken}\")")
            callback.onResult(response.items, null, response.nextToken)
        }
    }

    /**
     * Part of the PageKeyedDataSource - load the next page of a list.  This is called after
     * the loadInitial() has returned a response that includes a nextToken to load the next
     * page of items.
     */
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Note>) {
        Log.d(TAG, "loadAfter(${params.requestedLoadSize}, ${params.key})")
        dataService.loadNotes(params.requestedLoadSize, params.key) { response ->
            Log.d(TAG, "loadItems response = (${response.items.size}, \"${response.nextToken}\")")
            callback.onResult(response.items, response.nextToken)
        }
    }

    /**
     * Part of the PageKeyedDataSource - normally used to load the previous page, but this
     * version does not support paging backwards, so it becomes an invalidation.
     */
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Note>) {
        Log.d(TAG, "loadBefore(${params.requestedLoadSize}, ${params.key}")
        invalidate() // Our server-side API cannot go backwards along the list.
    }

    /**
     * The notes repository needs to do non-paged list type operations as well.  These operations
     * are passed through directly to the data service.  However, deletions (and saves) affect
     * the list, so we must ensure that the list is invalidated when they succeed.  This call is
     * to delete an item.
     */
    fun deleteItem(noteId: String, callback: () -> Unit) {
        dataService.deleteNote(noteId) { result ->
            if (result) invalidate() // If the note was deleted, the list is no longer valid
            callback()
        }
    }

    /**
     * Obtain a single item from the data service.
     */
    fun getItem(noteId: String, callback: (Note?) -> Unit) = dataService.getNote(noteId, callback)

    /**
     * The notes repository needs to do non-paged list type operations as well.  These operations
     * are passed through directly to the data service.  However, deletions (and saves) affect
     * the list, so we must ensure that the list is invalidated when they succeed.  This call is
     * to save an item.
     */
    fun saveItem(note: Note, callback: () -> Unit) {
        dataService.saveNote(note) { result ->
            if (result != null) invalidate() // If the note was saved, the list is no longer valid
            callback()
        }
    }
}
