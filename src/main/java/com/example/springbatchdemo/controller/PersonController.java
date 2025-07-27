package com.example.springbatchdemo.controller;

import static com.example.springbatchdemo.constants.ApplicationConstants.JOB_FAILED_LOG;
import static com.example.springbatchdemo.constants.ApplicationConstants.JOB_START_LOG;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

  private final JobLauncher jobLauncher;
  private final Job peopleInsertJob;

  @PostMapping("/import")
  public ResponseEntity<String> importPeople() {
    JobParameters jobParameters = new JobParametersBuilder()
            .addLong("startAt", System.currentTimeMillis())
            .toJobParameters();
    try {
      JobExecution jobExecution = jobLauncher.run(peopleInsertJob, jobParameters);
      log.info(JOB_START_LOG, peopleInsertJob.getName());
      return ResponseEntity.ok(jobExecution.getStatus().name());
    } catch (Exception e) {
      log.error(JOB_FAILED_LOG, e.getMessage());
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
              .body(JOB_FAILED_LOG + e.getMessage());
    }
  }


}
