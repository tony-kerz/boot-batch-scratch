package com.kerz.batch1

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class HttpClientConfiguration {
  
  static final Logger log = LoggerFactory.getLogger(BatchTestConfiguration)
  
  @Bean
  public HttpClient httpClient() {
    HttpClients.createDefault()
  }
}