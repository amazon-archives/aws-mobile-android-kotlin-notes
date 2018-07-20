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

/**
 * This class holds app-wide constants.  The most notable reasons for something appearing
 * in this file are the following:
 *
 * 1) The value is the name of a field that is passed as part of a Bundle from activity to activity.
 * 2) The value is an operation ID used when the application calls out to the Android platform to
 *    complete an operation (e.g. take a photo or receive a push notification).
 */
class Constants {
    companion object {
        // The name of the field used to hold the ID of the note when calling NoteDetailActivity
        const val ITEM_ID : String = "noteId"
    }
}