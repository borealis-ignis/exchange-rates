package com.borealis.erates.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;
import com.borealis.erates.io.PropertyFiles;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:" + PropertyFiles.ERATES_PROPERTIES, ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class ApplicationConfiguration {
	
}
