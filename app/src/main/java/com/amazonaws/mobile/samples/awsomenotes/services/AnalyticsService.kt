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

interface AnalyticsService {
    /**
     * Records a start session event into the analytics stream
     */
    fun startSession()

    /**
     * Records a stop session event into the analytics stream
     */
    fun stopSession()

    /**
     * Record a custom event into the analytics stream
     *
     * @param eventName the custom event name
     * @param attributes a list of key-value pairs for recording string attributes
     * @param metrics a list of key-value pairs for recording numeric metrics
     */
    fun recordEvent(eventName: String, attributes: Map<String,String>? = null, metrics: Map<String,Double>? = null)
}
