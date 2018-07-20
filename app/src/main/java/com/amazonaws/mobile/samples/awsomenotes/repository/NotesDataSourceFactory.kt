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

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.amazonaws.mobile.samples.awsomenotes.models.Note
import com.amazonaws.mobile.samples.awsomenotes.services.DataService

/**
 * Factory method to create data sources.   When the NotesDataSource is invalidated (because of
 * reverse paging or because the list has been altered), we have to create a new data source.
 */
class NotesDataSourceFactory(private val dataService: DataService) : DataSource.Factory<String, Note>() {
    private val mDataSource = MutableLiveData<NotesDataSource>()
    val currentDataSource : LiveData<NotesDataSource> = mDataSource

    override fun create(): DataSource<String, Note> {
        val dataSource = NotesDataSource(dataService)
        mDataSource.postValue(dataSource)
        return dataSource
    }
}
