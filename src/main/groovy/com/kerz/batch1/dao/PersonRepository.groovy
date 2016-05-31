package com.kerz.batch1.dao

import org.springframework.stereotype.Repository

import com.kerz.batch1.domain.Person
import com.kerz.orient.OrientRepository

@Repository
class PersonRepository extends OrientRepository<Person, String> {
}
