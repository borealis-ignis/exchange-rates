package com.borealis.erates.supplier.prior.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:priorb.properties", ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class PriorConfiguration {

}
