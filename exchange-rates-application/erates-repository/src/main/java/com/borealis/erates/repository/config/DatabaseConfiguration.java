package com.borealis.erates.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;
import com.borealis.erates.io.PropertyFiles;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:" + PropertyFiles.DB_PROPERTIES, ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class DatabaseConfiguration {
	
}
