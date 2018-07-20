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
package com.amazonaws.mobile.samples.awsomenotes.services.mock

import android.util.Log
import com.amazonaws.mobile.samples.awsomenotes.TAG
import com.amazonaws.mobile.samples.awsomenotes.models.Note
import com.amazonaws.mobile.samples.awsomenotes.models.PagedListConnectionResponse
import com.amazonaws.mobile.samples.awsomenotes.services.DataService

/**
 * A mock data store.  This will create 30 notes so you can see the scrolling action,
 * but otherwise acts as the data service.  This should be easily rewritten to use
 * an actual cloud API.
 *
 * The backing store for this data service is a simple in-memory array of items.
 */
class MockDataService : DataService {
    private val items = ArrayList<Note>()

    init {
        // Initialize items so we have a list that we can test scrolling immediately
        for (i in 1 .. 30) {
            val item = Note().apply {
                title = "Note $i"
                content = "Content for note $i"
            }
            items.add(item)
        }
    }

    /**
     * This simulates an API call to a network service that returns paged data.  We will
     * replace this with a real call later on.
     */
    override fun loadNotes(limit: Int, after: String?, callback: (PagedListConnectionResponse<Note>) -> Unit) {
        Log.d(TAG, "loadItems($limit, \"$after\")")
        check(limit in 1..100) { "Limit must be a reasonable positive number" }

        var firstItem = 0
        if (after != null) {
            // find the item offset for the after token
            firstItem = items.indexOfFirst { note -> note.noteId == after }
            if (firstItem < 0) { // We didn't find the after item, so return empty list
                callback(PagedListConnectionResponse(emptyList(), null))
                return
            }
            firstItem++ // We want the item after the item requested
        }
        Log.d(TAG, "loadItems(): firstItem = $firstItem, items.size = ${items.size}")
        // If the firstItem is beyond the end of the list, then return an empty list
        if (firstItem > items.size - 1) {
            Log.d(TAG, "loadItems(): asking for beyond end - return empty list")
            callback(PagedListConnectionResponse(emptyList(), null))
            return
        }

        // Number of items is either limit or from firstItem to the end of the list, whichever is smaller
        val nItems = minOf(limit, items.size - firstItem)
        Log.d(TAG, "loadItems(): nItems = $nItems")
        if (nItems == 0) {
            Log.d(TAG, "loadItems(): no items, so return empty list")
            callback(PagedListConnectionResponse(emptyList(), null))
            return
        }
        val sublist = items.subList(firstItem, firstItem + nItems).toList()
        val nextToken: String? = if (firstItem + nItems - 1 == items.size) null else sublist.last().noteId
        Log.d(TAG, "loadItems(): returning ${sublist.size} items, nextToken = $nextToken")
        callback(PagedListConnectionResponse(sublist, nextToken))
    }

    /**
     * Load a single note from the current list of notes
     *
     * @param noteId the request ID
     * @param callback the response from the server
     */
    override fun getNote(noteId: String, callback: (Note?) -> Unit) {
        Log.d(TAG, "getNote($noteId)")
        check(noteId.isNotBlank())
        val itemIndex = items.indexOfFirst { item -> item.noteId == noteId }
        if (itemIndex >= 0)
            callback(items[itemIndex])
        else
            callback(null)
    }

    /**
     * Save a note to the backing store
     *
     * @param note the note to be saved
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    override fun saveNote(note: Note, callback: (Note?) -> Unit) {
        Log.d(TAG, "saveNote(${note.noteId})")
        check(note.noteId.isNotBlank())

        val itemIndex = items.indexOfFirst { item -> item.noteId == note.noteId }
        if (itemIndex >= 0)
            items[itemIndex] = note
        else
            items.add(note)
        callback(note)
    }

    /**
     * Delete a note from the backing store
     *
     * @param noteId the ID of the note to be deleted
     * @param callback the response from the server (Boolean = true indicates success)
     */
    override fun deleteNote(noteId: String, callback: (Boolean) -> Unit) {
        Log.d(TAG, "deleteNote($noteId)")
        check(noteId.isNotBlank())

        val itemIndex = items.indexOfFirst { item -> item.noteId == noteId }
        if (itemIndex >= 0) {
            items.removeAt(itemIndex)
            callback(true)
        } else {
            callback(false)
        }
    }
}
