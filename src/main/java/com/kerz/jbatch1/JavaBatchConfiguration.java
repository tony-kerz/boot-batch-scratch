package com.kerz.jbatch1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.kerz.jbatch1.batch.JavaJobCompletionNotificationListener;
import com.kerz.jbatch1.batch.JavaPersonItemProcessor;
import com.kerz.jbatch1.domain.JavaPerson;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JavaBatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  public DataSource dataSource;

  // tag::readerwriterprocessor[]
  @Bean
  public FlatFileItemReader<JavaPerson> reader() {
    FlatFileItemReader<JavaPerson> reader = new FlatFileItemReader<JavaPerson>();
    reader.setResource(new ClassPathResource("sample-data.csv"));
    reader.setLineMapper(new DefaultLineMapper<JavaPerson>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[] { "firstName", "lastName" });
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<JavaPerson>() {{
        setTargetType(JavaPerson.class);
      }});
    }});
    return reader;
  }

  @Bean
  public JavaPersonItemProcessor processor() {
    return new JavaPersonItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<JavaPerson> writer() {
    JdbcBatchItemWriter<JavaPerson> writer = new JdbcBatchItemWriter<JavaPerson>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<JavaPerson>());
    writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
    writer.setDataSource(dataSource);
    return writer;
  }
  // end::readerwriterprocessor[]

  // tag::listener[]

  @Bean
  public JobExecutionListener listener() {
    return new JavaJobCompletionNotificationListener(new JdbcTemplate(dataSource));
  }

  // end::listener[]

  // tag::jobstep[]
  @Bean
  public Job importUserJob() {
    return jobBuilderFactory.get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener())
        .flow(step1())
        .end()
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .<JavaPerson, JavaPerson> chunk(10)
        .reader(reader())
        .processor((ItemProcessor<JavaPerson,JavaPerson>)processor())
        .writer(writer())
        .build();
  }
  // end::jobstep[]
}
