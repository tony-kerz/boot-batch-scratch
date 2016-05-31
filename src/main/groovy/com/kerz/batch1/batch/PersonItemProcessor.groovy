package com.kerz.batch1.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.Assert

import com.kerz.batch1.domain.Person
import com.kerz.geo.Geocoder
import com.kerz.geo.Point
import com.kerz.orient.domain.OPoint

public class PersonItemProcessor implements ItemProcessor<Person, Person>{
  static Logger log = LoggerFactory.getLogger(PersonItemProcessor)

  @Autowired
  Geocoder geocoder
  
  @Override
  public Person process(Person person) throws Exception {
    log.debug("process: person=$person")
    Assert.isNull(person.point, 'point unexpected')
    Point point = geocoder.geocode(person.address)
    log.debug("process: point=$point")
    person.point = new OPoint(coordinates: [point.lat, point.lon])
    person
  }
}
