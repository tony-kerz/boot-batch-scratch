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
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.kerz.batch1.dao.PersonRepository
import com.kerz.batch1.domain.Person
import com.kerz.geo.HttpClientGeocoderConfiguration
import com.kerz.orient.OrientHelper
import com.kerz.orient.OrientRepositoryConfiguration

@RunWith(SpringJUnit4ClassRunner.class)
// http://stackoverflow.com/a/27396456/2371903
@SpringApplicationConfiguration(classes = [
  OrientRepositoryConfiguration,
  PersonRepository,
  HttpClientConfiguration,  
  HttpClientGeocoderConfiguration,
  BatchConfiguration, 
  BatchTestConfiguration
  ]
)
class BatchTest {

  Logger log = LoggerFactory.getLogger(BatchTest)
  
  @Autowired
  Environment env
  
  @Autowired
  JobLauncherTestUtils jobLauncherTestUtils
  
  @Autowired
  PersonRepository personRepository
  
  @Autowired
  OrientHelper oHelper
  
  @Before
  void setUp() {
    log.info("set-up: env=$env")
    oHelper.submitSql('drop class person unsafe')
    oHelper.submitSql('create class Person extends V')
    oHelper.submitSql('create property person.point embedded OPoint')
    
    int idx = 0
    [
      '151 Farmington Ave, Hartford CT, 06108',
      '333 East 69th Street, New York NY 10021',
      '3728 Dawson Street, Oakland PA 18847',
      '1600 Pennsylvania Ave NW, Washington, DC 20500',
      '1060 W Addison St, Chicago, IL 60613'
    ].each {
      idx += 1
      log.debug("set-up: idx=$idx, address=$it")
      personRepository.save(new Person(firstName: "first-name-$idx", lastName: "last-name-$idx", address: it))
    }
  }

  @After
  void tearDown() {
    log.info('tear-down')
    //personRepository.deleteAll()
    oHelper.g.commit()
  }

  @Test
  void shouldWork() {
    def people = personRepository.findAll()
    assertTrue(people.size > 0)
    people.each {
      assertNull(it.point)
    }

    jobLauncherTestUtils.launchJob()
    
    people = personRepository.findAll()
    assertTrue(people.size > 0)
    people.each {
      assertNotNull(it.point)
    }
  }
}
