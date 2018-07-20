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

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.models.Note

/**
 * When implementing a RecyclerView for displaying a list, there is a
 * view holder that implements the view.  The recycler view adapter will
 * bind the view holder to the UI.
 */
class NoteListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val titleField: TextView = view.findViewById(R.id.list_title)
    private val idField: TextView = view.findViewById(R.id.list_id)

    var note: Note? = null
        set(value) {
            field = value
            titleField.text = note?.title ?: "null"
            idField.text = note?.noteId ?: "undefined"
        }
}
