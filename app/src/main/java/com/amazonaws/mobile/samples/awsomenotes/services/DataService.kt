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
package com.amazonaws.mobile.samples.awsomenotes.services

import com.amazonaws.mobile.samples.awsomenotes.models.Note
import com.amazonaws.mobile.samples.awsomenotes.models.PagedListConnectionResponse

/**
 * Definition of a data service.  This maps to an API definition on the cloud backend.
 * Each call should be async and run on a background thread.
 */
interface DataService {
    /**
     * Load a single page of notes.
     *
     * @param limit the requested number of items
     * @param after the "next token" from a prior call
     * @param callback the response from the server
     */
    fun loadNotes(limit: Int = 20, after: String? = null, callback: (PagedListConnectionResponse<Note>) -> Unit)

    /**
     * Load a single note
     *
     * @param noteId the request ID
     * @param callback the response from the server
     */
    fun getNote(noteId: String, callback: (Note?) -> Unit)

    /**
     * Save a note to the backing store
     *
     * @param note the note to be saved
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    fun saveNote(note: Note, callback: (Note?) -> Unit)

    /**
     * Delete a note from the backing store
     *
     * @param noteId the ID of the note to be deleted
     * @param callback the response from the server (Boolean = true indicates success)
     */
    fun deleteNote(noteId: String, callback: (Boolean) -> Unit)

}