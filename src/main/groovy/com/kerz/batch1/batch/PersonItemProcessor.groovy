package com.kerz.batch1.batch

import org.apache.http.client.HttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.Assert

import com.kerz.batch1.domain.Person

public class PersonItemProcessor implements ItemProcessor<Person, Person>{
  static Logger log = LoggerFactory.getLogger(PersonItemProcessor)

  @Autowired
  HttpClient httpClient
  
  @Override
  public Person process(Person person) throws Exception {
    // https://search.mapzen.com/v1/search?text=06108&api_key=search-ND7BVJ&boundary.country=USA&size=1
    Assert.notNull(httpClient)
    log.info("process: last-name=$person.lastName")
    person
  }
}
