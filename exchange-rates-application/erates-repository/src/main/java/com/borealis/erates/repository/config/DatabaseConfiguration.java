package com.borealis.erates.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySources({
	@PropertySource(value="classpath:db.properties", ignoreResourceNotFound=true)
})
public class DatabaseConfiguration {
	
}
