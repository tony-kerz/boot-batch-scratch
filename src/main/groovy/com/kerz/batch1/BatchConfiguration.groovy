package com.kerz.batch1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort

import com.kerz.batch1.batch.JobCompletionNotificationListener
import com.kerz.batch1.batch.PersonItemProcessor
import com.kerz.batch1.dao.PersonSqlRepository
import com.kerz.batch1.domain.Person
import com.kerz.orient.tx.OrientTransactionManager

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
  static final Logger log = LoggerFactory.getLogger(BatchConfiguration)
  
  int commitInterval = 2

  @Autowired
  JobBuilderFactory jobBuilderFactory

  @Autowired
  PersonSqlRepository personRepository
  
  @Autowired
  JobRepository jobRepository
  
  @Autowired
  OrientTransactionManager transactionManager
  
  @Bean
  OrientTransactionManager transactionManager() {
    new OrientTransactionManager()
  }
  
  public StepBuilderFactory stepBuilderFactory() {
    transactionManager.g.autoStartTx = false
    new StepBuilderFactory(jobRepository, transactionManager)
  }

  @Bean
  def reader() {
    def reader = new RepositoryItemReader<Person>()
    reader.pageSize = commitInterval
    reader.repository = personRepository
    reader.methodName = 'findAll'
    reader.sort = [lastName: Sort.Direction.ASC]
    reader
  }
  
  @Bean
  PersonItemProcessor processor() {
    new PersonItemProcessor()
  }
  
  @Bean
  def writer() {
    def writer = new RepositoryItemWriter<Person>()
    writer.repository = personRepository
    writer.methodName = 'save'
    writer
  }
  
  @Bean
  JobExecutionListener listener() {
    new JobCompletionNotificationListener()
  }

  @Bean
  Job importUserJob() {
    jobBuilderFactory.get('importUserJob')
        .incrementer(new RunIdIncrementer())
        .listener(listener())
        .flow(step1())
        .end()
        .build()
  }

  @Bean
  Step step1() {
    stepBuilderFactory().get('step1')
        .<Person, Person> chunk(commitInterval)
        .reader(reader())
        .processor((ItemProcessor<Person,Person>)processor())
        .writer(writer())
        .build()
  }
}
