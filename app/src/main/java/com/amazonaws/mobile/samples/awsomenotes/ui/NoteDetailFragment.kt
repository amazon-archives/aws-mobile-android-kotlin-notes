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
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amazonaws.mobile.samples.awsomenotes.Constants
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.databinding.NoteDetailBinding
import com.amazonaws.mobile.samples.awsomenotes.viewmodels.NoteDetailViewModel
import org.koin.android.architecture.ext.viewModel

class NoteDetailFragment : Fragment() {
    /**
     * View model for the fragment
     */
    private val viewModel by viewModel<NoteDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If we were passed the noteId, then pass it to the viewModel so that
        // it can be loaded.  This will automatically update the UI
        arguments?.let {
            viewModel.setNoteId(it.getString(Constants.ITEM_ID))
        }
    }

    /**
     * Create a new view
     *
     * This fragment uses the data binding library to make the data within the view model appear
     * within the layout.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Bind the viewModel to the UI
        val binding: NoteDetailBinding = DataBindingUtil.inflate(inflater, R.layout.note_detail, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        // We need to be careful about doing a "two-way binding" in this case.  We don't want
        // that because it can cause cyclical API calls, which can cascade out of control.
        // Instead, we use one-way data binding and trap the changes the other way so that we
        // do the appropriate API calls.
        viewModel.title.observe(this, Observer {
            viewModel.saveNote(viewModel.note.value!!.apply { title = it!! })
        })
        viewModel.content.observe(this, Observer {
            viewModel.saveNote(viewModel.note.value!!.apply { content = it!! })
        })

        return binding.root
    }
}