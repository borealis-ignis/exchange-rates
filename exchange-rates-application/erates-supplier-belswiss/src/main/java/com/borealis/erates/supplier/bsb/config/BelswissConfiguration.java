package com.borealis.erates.supplier.bsb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:bsb.properties", ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class BelswissConfiguration {
	
}
