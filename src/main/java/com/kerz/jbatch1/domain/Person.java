package com.kerz.jbatch1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Id;
import javax.persistence.Version;

@JsonIgnoreProperties(value = {"handler"})
public class Person {

    @Id
    private String id;
    
    @Version
    @JsonIgnore
    private Long version;
    
    /**
     * Added to avoid a runtime error whereby the detachAll property is checked
     * for existence but not actually used.
     */
    private String detachAll;
    
    private String firstName;
    
    private String lastName;
    
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
