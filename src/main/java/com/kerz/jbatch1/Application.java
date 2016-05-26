package com.kerz.jbatch1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories;

@SpringBootApplication
@EnableOrientRepositories("com.kerz.jbatch1.dao")
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
