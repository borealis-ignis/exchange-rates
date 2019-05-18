package com.borealis.erates.updater.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.borealis.erates.io.ExternalPropertySourceFactory;
import com.borealis.erates.io.PropertyFiles;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySource(value="classpath:" + PropertyFiles.UPDATER_PROPERTIES, ignoreResourceNotFound=true, factory=ExternalPropertySourceFactory.class)
public class UpdaterConfiguration {
	
}
