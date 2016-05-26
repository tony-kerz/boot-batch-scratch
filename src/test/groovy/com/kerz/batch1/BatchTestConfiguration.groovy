package com.kerz.batch1

import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class BatchTestConfiguration {
  @Bean
  public JobLauncherTestUtils jobLauncherTestUtils() {
    new JobLauncherTestUtils()
  }
}