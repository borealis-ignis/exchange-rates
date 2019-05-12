package com.borealis.erates.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySources({
	@PropertySource(value="classpath:application.properties", ignoreResourceNotFound=true)
})
public class ApplicationConfiguration {
	
}
