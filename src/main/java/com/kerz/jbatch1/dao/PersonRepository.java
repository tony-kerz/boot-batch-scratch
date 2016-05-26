package com.kerz.jbatch1.dao;

import java.util.List;

import org.springframework.data.orient.commons.repository.annotation.Query;
import org.springframework.data.orient.object.repository.OrientObjectRepository;

import com.kerz.jbatch1.domain.Person;

public interface PersonRepository extends OrientObjectRepository<Person> {

  List<Person> findByFirstName(String firstName);

  @Query("select from person where lastName = ?")
  List<Person> findByLastName(String lastName);

  List<Person> findByAge(Integer age);
}
