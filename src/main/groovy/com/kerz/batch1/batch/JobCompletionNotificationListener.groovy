package com.kerz.batch1.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.Assert

import com.kerz.batch1.dao.PersonSqlRepository

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener)

  @Autowired
  PersonSqlRepository personRepository
  
  @Override
  void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info('!!! JOB FINISHED! Time to verify the results')

      Assert.notNull(personRepository)
    }
  }
}
