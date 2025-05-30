package com.jaeseung.springbatchbasic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

// Slack 이나 외부 API를 이용하여 알림 시스템 연결 가능
@Slf4j
public class JobLoggerListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job 시작: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job 종료: {} (상태: {})",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus());
    }
}
