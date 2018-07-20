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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.amazonaws.mobile.samples.awsomenotes.Constants
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.addFragmentInActivity
import com.amazonaws.mobile.samples.awsomenotes.services.AnalyticsService
import kotlinx.android.synthetic.main.activity_note_detail.*
import org.koin.android.ext.android.inject

class NoteDetailActivity : AppCompatActivity() {
    /**
     * Analytics Service
     */
    private val analyticsService: AnalyticsService by inject()

    /**
     * Android activity lifecycle handler that is called when the activity is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        // Set up the action bar so that it has a back button
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // If we have a valid request (i.e. ITEM_ID in the bundle), then pass it
        // along to a fragment.
        val fragment = NoteDetailFragment().apply {
            arguments = Bundle().apply { putString(Constants.ITEM_ID, intent.getStringExtra(Constants.ITEM_ID)) }
        }
        addFragmentInActivity(fragment, R.id.note_detail_container)
    }

    /**
     * Android activity lifecycle handler that is called when the UI is resumed.  This is
     * called after creation or the same intent is provided.
     */
    override fun onResume() {
        super.onResume()
        analyticsService.recordEvent("NoteDetailActivity")
    }

    /**
     * Event handler called when an item on the action bar is selected.  In this case, there
     * is only one item on the action bar (the back button).
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, NoteListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}