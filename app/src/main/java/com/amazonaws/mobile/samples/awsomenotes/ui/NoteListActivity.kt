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

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.amazonaws.mobile.samples.awsomenotes.Constants
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.TAG
import com.amazonaws.mobile.samples.awsomenotes.replaceFragmentInActivity
import com.amazonaws.mobile.samples.awsomenotes.services.AnalyticsService
import com.amazonaws.mobile.samples.awsomenotes.viewmodels.NoteListViewModel
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.note_list.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import java.util.*

class NoteListActivity : AppCompatActivity() {
    /**
     * If the device is running in two-pane mode, then this is set to true.  In two-pane mode,
     * the UI is a side-by-side, with the list on the left and the details on the right.  In
     * one-pane mode, the list and details are separate pages.  Two-pane mode is used on a
     * landscape tablet.
     */
    private var twoPane: Boolean = false

    /**
     * Inject the viewModel via dependency injection
     */
    private val viewModel by viewModel<NoteListViewModel>()

    /**
     * Analytics Service
     */
    private val analyticsService : AnalyticsService by inject()

    /**
     * Android Activity lifecycle method used to create the UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note_list)
        twoPane = (note_detail_container != null)

        // Configure the action bar
        setSupportActionBar(toolbar)
        toolbar.title = title

        // Add an item click handler to the floating action button for adding a note
        fab.setOnClickListener {
            loadNoteDetailFragment(UUID.randomUUID().toString())
        }

        // Create the adapter that will be used to load items into the recycler view
        val adapter = NoteListAdapter { note ->
            // The user clicked on a note in the list
            Log.d(TAG, "Clicked on note ${note.noteId}")
            loadNoteDetailFragment(note.noteId)
        }

        // Create the swipe-to-delete handler
        val swipeToDelete = ItemTouchHelper(SwipeToDelete(this) { note ->
            Log.d(TAG, "Item ${note.noteId} is being removed")
            viewModel.removeNote(note.noteId)
        })

        // Configure the note list
        swipeToDelete.attachToRecyclerView(note_list)
        note_list.adapter = adapter

        // Ensure the note list is updated whenever the repository is updated
        viewModel.notesList.observe(this, Observer { notes ->
            adapter.submitList(notes)
        })
    }

    /**
     * Android Activity lifecycle method used when the UI is resumed (either after creating
     * it for the first time or on return from the NoteDetailActivity)
     */
    override fun onResume() {
        super.onResume()
        analyticsService.recordEvent("NoteListActivity", mapOf("twoPane" to "$twoPane"))
    }

    /**
     * Loads the note details
     */
    private fun loadNoteDetailFragment(noteId: String) {
        if (twoPane) {
            val fragment = NoteDetailFragment().apply {
                arguments = Bundle().apply { putString(Constants.ITEM_ID, noteId) }
            }
            replaceFragmentInActivity(fragment, R.id.note_detail_container)
        } else {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra(Constants.ITEM_ID, noteId)
            }
            startActivity(intent)
        }
    }
}