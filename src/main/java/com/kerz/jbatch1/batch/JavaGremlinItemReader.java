package com.kerz.jbatch1.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.kerz.jbatch1.domain.JavaPerson;

public class JavaGremlinItemReader implements ItemReader<JavaPerson> {
  @Override
  public JavaPerson read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    return null;
  }
}
