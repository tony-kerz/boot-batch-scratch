package com.kerz.batch1.domain

import javax.persistence.Id
import javax.persistence.Version

import org.apache.commons.lang3.builder.ReflectionToStringBuilder

public class Person {

  @Id
  String id
  
  @Version
  Long version
  
  /**
   * Added to avoid a runtime error whereby the detachAll property is checked
   * for existence but not actually used.
   */
  String detachAll
  
  String lastName
  String firstName
  
  Integer age
  String address
  Float lat
  Float lon

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this)
  }
}
