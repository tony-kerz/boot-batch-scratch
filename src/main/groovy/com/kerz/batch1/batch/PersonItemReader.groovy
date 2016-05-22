package com.kerz.batch1.batch

import com.kerz.batch1.domain.Person
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.NonTransientResourceException
import org.springframework.batch.item.ParseException
import org.springframework.batch.item.UnexpectedInputException

public class PersonItemReader implements ItemReader<Person> {
  @Override
  public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    return null
  }
}
