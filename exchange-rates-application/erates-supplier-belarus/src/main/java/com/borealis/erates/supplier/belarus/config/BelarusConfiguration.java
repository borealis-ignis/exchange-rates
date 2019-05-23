package com.borealis.erates.supplier.belarus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:belarusb.properties", ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class BelarusConfiguration {
	
}
