package com.kerz.batch1.batch

import com.kerz.batch1.domain.Person
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.SQLException

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener)

  private final JdbcTemplate jdbcTemplate

  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info('!!! JOB FINISHED! Time to verify the results')

      def results = jdbcTemplate.query('SELECT first_name, last_name FROM people', new RowMapper<Person>() {
        @Override
        public Person mapRow(ResultSet rs, int row) throws SQLException {
          new Person(firstName: rs.getString(1), lastName: rs.getString(2))
        }
      })

      results.each {
        log.info("Found <${it}> in the database.")
      }

    }
  }
}
