package com.borealis.erates.supplier.bps.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:bps.properties", ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class BpsConfiguration {
	
}
