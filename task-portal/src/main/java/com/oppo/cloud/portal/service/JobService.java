/*
 * Copyright 2023 OPPO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oppo.cloud.portal.service;

import com.oppo.cloud.common.domain.opensearch.JobInstance;
import com.oppo.cloud.common.domain.job.Datum;
import com.oppo.cloud.portal.domain.diagnose.Item;
import com.oppo.cloud.portal.domain.diagnose.runtime.ChartData;
import com.oppo.cloud.portal.domain.diagnose.runtime.TableData;
import com.oppo.cloud.portal.domain.log.LogInfo;
import com.oppo.cloud.portal.domain.task.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JobService
 */
public interface JobService {

    /**
     * search jobs
     */
    JobsResponse searchJobs(JobsRequest request) throws Exception;

    /**
     * search job apps
     */
    JobAppsRespone searchJobApps(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * search job diagnose
     */
    List<String> searchJobDiagnose(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * search log  information
     */
    Item<TableData<LogInfo>> searchLogInfo(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * search duration trend
     */
    Item<ChartData> searchDurationTrend(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * search job datum
     */
    Item<Datum> searchJobDatum(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * job trend graph
     */
    TrendGraph getGraph(JobsRequest request) throws Exception;

    /**
     * app diagnose info
     */
    List<Map<String, Item>> searchAppDiagnoseInfo(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * update task state
     */
    void updateJobState(JobDetailRequest jobDetailRequest) throws Exception;

    /**
     * get job instance
     */
    JobInstance getJobInstance(String projectName, String flowName, String taskName,
                               Date executionDate) throws Exception;
}
