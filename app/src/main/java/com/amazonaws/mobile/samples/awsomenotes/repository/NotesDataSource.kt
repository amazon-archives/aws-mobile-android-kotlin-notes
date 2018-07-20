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
