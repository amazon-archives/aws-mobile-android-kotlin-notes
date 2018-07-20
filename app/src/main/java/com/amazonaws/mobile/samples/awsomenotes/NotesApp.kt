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
package com.amazonaws.mobile.samples.awsomenotes

import android.app.Application
import com.amazonaws.mobile.samples.awsomenotes.repository.NotesRepository
import com.amazonaws.mobile.samples.awsomenotes.services.AnalyticsService
import com.amazonaws.mobile.samples.awsomenotes.services.DataService
import com.amazonaws.mobile.samples.awsomenotes.services.mock.MockAnalyticsService
import com.amazonaws.mobile.samples.awsomenotes.services.mock.MockDataService
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * This is the entry point into the application.  The main function of this class within this
 * app is to set up dependency injection properly.
 */
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
            viewModel { NoteListViewModel(/* AnalyticsService */ get(), /* NotesRepository */ get()) }
            viewModel { NoteDetailViewModel(/* AnalyticsService */ get(), /* NotesRepository */ get()) }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Dependency Injection
        startKoin(this, listOf(servicesModule, repositoriesModule, viewModelsModule))
    }
}