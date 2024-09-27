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

package com.oppo.cloud.detect.service.impl;

import com.oppo.cloud.common.util.ui.TryNumberUtil;
import com.oppo.cloud.detect.service.SchedulerLogService;
import com.oppo.cloud.mapper.TaskApplicationMapper;
import com.oppo.cloud.model.TaskApplication;
import com.oppo.cloud.model.TaskApplicationExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of scheduling log interface.
 */
@Slf4j
@Service
public class SchedulerLogServiceImpl implements SchedulerLogService {

    @Value("${custom.schedulerType}")
    private String schedulerType;

    @Autowired
    private TaskApplicationMapper taskApplicationMapper;

    @Override
    public List<String> getSchedulerLog(String projectName, String flowName, String taskName, Date executionDate,
                                        Integer tryNum) {
        int maxRetries = 5;
        long delayMs = 60000; // 1 second delay

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            List<String> result = tryGetSchedulerLog(projectName, flowName, taskName, executionDate, tryNum);
            if (result != null) {
                return result;
            }

            if (attempt < maxRetries - 1) {
                try {
                    TimeUnit.MILLISECONDS.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Sleep interrupted", e);
                }
                log.info("Retrying getSchedulerLog (attempt {}/{})", attempt + 2, maxRetries);
            }
        }

        log.error(
                "Failed to find scheduler log after {} attempts. taskName:{}, flowName:{}, executionDate:{}, tryNum:{}",
                maxRetries, taskName, flowName, executionDate, tryNum);
        return null;
    }

    private List<String> tryGetSchedulerLog(String projectName, String flowName, String taskName, Date executionDate,
                                            Integer tryNum) {
        TaskApplicationExample taskApplicationExample = new TaskApplicationExample();
        taskApplicationExample.createCriteria()
                .andProjectNameEqualTo(projectName)
                .andFlowNameEqualTo(flowName)
                .andTaskNameEqualTo(taskName)
                .andExecuteTimeEqualTo(executionDate);
        List<TaskApplication> taskApplicationList =
                taskApplicationMapper.selectByExampleWithBLOBs(taskApplicationExample);
        
        if (taskApplicationList.size() != 0) {
            TaskApplication taskApplication = null;
            for (TaskApplication temp : taskApplicationList) {
                temp.setRetryTimes(TryNumberUtil.updateTryNumber(temp.getRetryTimes(), schedulerType));
                if (temp.getRetryTimes().equals(tryNum)) {
                    taskApplication = temp;
                    break;
                }
            }
            // The number of retries for tasks without a scheduling cycle is set to 0 by default.
            if (taskApplication == null) {
                taskApplication = taskApplicationList.get(0);
            }
            if (!StringUtils.isEmpty(taskApplication.getLogPath())) {
                return Arrays.asList(taskApplication.getLogPath().split(","));
            }
        }
        return null;
    }
}
