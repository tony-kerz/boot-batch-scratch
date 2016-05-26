package com.kerz.batch1

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.orient.commons.core.OrientTransactionManager
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories
import org.springframework.data.orient.object.OrientObjectDatabaseFactory
import org.springframework.data.orient.object.OrientObjectTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement

import com.kerz.batch1.domain.Person

@Configuration
@EnableTransactionManagement
@EnableOrientRepositories(basePackages = 'com.kerz.batch1')
public class DataConfiguration {
  
  static final Logger log = LoggerFactory.getLogger(DataConfiguration)
  
  @Bean
  public OrientObjectDatabaseFactory factory() {
    OrientObjectDatabaseFactory factory =  new OrientObjectDatabaseFactory()

    factory.setUrl('remote:192.168.99.100/TestGraph1')
    factory.setUsername('root')
    factory.setPassword('s3cret')

    factory
  }

  @Bean
  public OrientTransactionManager transactionManager() {
    new OrientTransactionManager(factory())
  }

  @Bean
  public OrientObjectTemplate objectTemplate() {
    new OrientObjectTemplate(factory())
  }


  @PostConstruct
  public void registerEntities() {
    def entities = [Person]
    entities.each {
      log.debug("register-entities: [$it]")
      factory().db().getEntityManager().registerEntityClass(it)
    }
  }
}