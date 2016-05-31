package com.kerz.batch1.domain

import groovy.transform.EqualsAndHashCode

import com.kerz.orient.domain.GElement
import com.kerz.orient.domain.OPoint

@EqualsAndHashCode
class Person extends GElement {
  String firstName
  String lastName
  String address
  OPoint point
}
