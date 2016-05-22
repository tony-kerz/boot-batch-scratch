package com.kerz.jbatch1.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.kerz.jbatch1.domain.JavaPerson;

public class JavaPersonItemProcessor implements ItemProcessor<JavaPerson, JavaPerson>{
  private static final Logger log = LoggerFactory.getLogger(JavaPersonItemProcessor.class);

  @Override
  public JavaPerson process(final JavaPerson person) throws Exception {
    final String firstName = person.getFirstName().toUpperCase();
    final String lastName = person.getLastName().toUpperCase();

    final JavaPerson transformedPerson = new JavaPerson(firstName, lastName);

    log.info("Converting (" + person + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
