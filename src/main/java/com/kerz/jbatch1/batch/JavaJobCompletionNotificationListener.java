package com.kerz.jbatch1.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.kerz.jbatch1.domain.JavaPerson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JavaJobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JavaJobCompletionNotificationListener.class);

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JavaJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      List<JavaPerson> results = jdbcTemplate.query("SELECT first_name, last_name FROM people", new RowMapper<JavaPerson>() {
        @Override
        public JavaPerson mapRow(ResultSet rs, int row) throws SQLException {
          return new JavaPerson(rs.getString(1), rs.getString(2));
        }
      });

      for (JavaPerson person : results) {
        log.info("Found <" + person + "> in the database.");
      }

    }
  }
}
