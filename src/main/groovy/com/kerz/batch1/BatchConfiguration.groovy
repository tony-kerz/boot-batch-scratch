package com.kerz.batch1

import com.kerz.batch1.batch.JobCompletionNotificationListener
import com.kerz.batch1.batch.PersonItemProcessor
import com.kerz.batch1.domain.Person
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory

  @Autowired
  public StepBuilderFactory stepBuilderFactory

  @Autowired
  public DataSource dataSource

  @Bean
  public FlatFileItemReader<Person> reader() {
    FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>()
    reader.setResource(new ClassPathResource('sample-data.csv'))
    reader.setLineMapper(new DefaultLineMapper<Person>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(['firstName', 'lastName'] as String[])
      }})
      setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
        setTargetType(Person.class)
      }})
    }})
    reader
  }

  @Bean
  public PersonItemProcessor processor() {
    new PersonItemProcessor()
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer() {
    JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>()
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
    writer.setSql('INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)')
    writer.setDataSource(dataSource)
    writer
  }

  @Bean
  public JobExecutionListener listener() {
    new JobCompletionNotificationListener(new JdbcTemplate(dataSource))
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
