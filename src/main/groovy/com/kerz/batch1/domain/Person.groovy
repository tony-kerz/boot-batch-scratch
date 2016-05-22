package com.kerz.batch1.domain

import org.apache.commons.lang3.builder.ReflectionToStringBuilder

public class Person {
  String lastName
  String firstName

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this)
  }
}
