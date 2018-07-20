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
package com.amazonaws.mobile.samples.awsomenotes.models

/**
 * Model class used as the response to an API call.  In this particular instances, it's
 * a paged list response where the next page is denoted by a "nextToken" - ostensibly a
 * string, but really an opaque blob that you should not mess with - just submit it along
 * with the API call to get the next page.
 */
data class PagedListConnectionResponse<out T>(val items: List<T>, val nextToken: String?)
