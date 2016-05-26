package com.kerz.batch1

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.kerz.batch1.dao.PersonRepository
import com.kerz.batch1.domain.Person

@RunWith(SpringJUnit4ClassRunner.class)
// http://stackoverflow.com/a/27396456/2371903
@SpringApplicationConfiguration(classes = [
  BatchConfiguration, 
  DataConfiguration, 
  HttpClientConfiguration,
  BatchTestConfiguration
  ]
)
//@PropertySource('classpath:application.test.properties')
class BatchTest {

  Logger log = LoggerFactory.getLogger(BatchTest)
  
  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils
  
  @Autowired
  PersonRepository personRepository
  
  @Before
  void setUp() throws Exception {
    assertNotNull(personRepository)
    def person = new Person(firstName: 'fred', lastName: 'jones', age: 10, address: '151 Farmington Ave, Hartford CT, 06108, USA')
    personRepository.save(person)
  }

  @After
  void tearDown() throws Exception {
    assertNotNull(personRepository)
    //personRepository.deleteAll()
  }

  @Test
  void shouldWork() throws Exception {
    
    // make sure setup worked
    def people = personRepository.findAll()
    people.each {
      log.debug("person=$it")
    }
    assertTrue('people required', people.size > 0)
    def person = people.get(0)
    
    assertNotNull('utils required', jobLauncherTestUtils)
    jobLauncherTestUtils.launchJob()
    
    person = personRepository.findOne(person.id)
    assertNotNull('lat required', person.lat)
    assertNotNull('lon required', person.lat)
  }
}
