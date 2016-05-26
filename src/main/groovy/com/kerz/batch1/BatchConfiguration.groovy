package com.kerz.batch1

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort

import com.kerz.batch1.batch.JobCompletionNotificationListener
import com.kerz.batch1.batch.PersonItemProcessor
import com.kerz.batch1.dao.PersonRepository
import com.kerz.batch1.domain.Person

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory

  @Autowired
  public StepBuilderFactory stepBuilderFactory
  
  @Autowired
  private PersonRepository personRepository
  
  @Bean
  public def reader() {
    def reader = new RepositoryItemReader<Person>()
    reader.pageSize = 5
    reader.repository = personRepository
    reader.methodName = 'findAll'
    reader.sort = [lastName: Sort.Direction.ASC]
    reader
  }
  
  @Bean
  public PersonItemProcessor processor() {
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
  public JobExecutionListener listener() {
    new JobCompletionNotificationListener()
  }

  @Bean
  public Job importUserJob() {
    jobBuilderFactory.get('importUserJob')
        .incrementer(new RunIdIncrementer())
        .listener(listener())
        .flow(step1())
        .end()
        .build()
  }

  @Bean
  public Step step1() {
    stepBuilderFactory.get('step1')
        .<Person, Person> chunk(10)
        .reader(reader())
        .processor((ItemProcessor<Person,Person>)processor())
        .writer(writer())
        .build()
  }
}
