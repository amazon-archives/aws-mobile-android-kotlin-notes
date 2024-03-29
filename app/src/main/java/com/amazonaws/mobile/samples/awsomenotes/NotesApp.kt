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
package com.amazonaws.mobile.samples.awsomenotes

import android.app.Application
import com.amazonaws.mobile.samples.awsomenotes.repository.NotesRepository
import com.amazonaws.mobile.samples.awsomenotes.services.AnalyticsService
import com.amazonaws.mobile.samples.awsomenotes.services.DataService
import com.amazonaws.mobile.samples.awsomenotes.services.mock.MockAnalyticsService
import com.amazonaws.mobile.samples.awsomenotes.services.mock.MockDataService
import com.amazonaws.mobile.samples.awsomenotes.viewmodels.NoteDetailViewModel
import com.amazonaws.mobile.samples.awsomenotes.viewmodels.NoteListViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * This is the entry point into the application.  The main function of this class within this
 * app is to set up dependency injection properly.
 */
@Suppress("unused")
class NotesApp : Application() {
    companion object {
        // List of services that this app communicates with.
        private val servicesModule : Module = applicationContext {
            bean { MockAnalyticsService() as AnalyticsService }
            bean { MockDataService() as DataService }
        }

        // List of repositories for holding data
        private val repositoriesModule : Module = applicationContext {
            bean { NotesRepository(/* DataService */ get()) }
        }

        // List of view models that are used by activities and fragments
        private val viewModelsModule : Module = applicationContext {
            viewModel { NoteListViewModel(/* NotesRepository */ get()) }
            viewModel { NoteDetailViewModel(/* NotesRepository */ get()) }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Dependency Injection
        startKoin(this, listOf(servicesModule, repositoriesModule, viewModelsModule))
    }
}