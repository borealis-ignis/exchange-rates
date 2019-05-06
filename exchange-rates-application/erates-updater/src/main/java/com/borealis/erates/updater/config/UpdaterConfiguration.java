package com.borealis.erates.updater.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySources({
	@PropertySource(value="classpath:updater.properties", ignoreResourceNotFound=true)
})
public class UpdaterConfiguration {
	
}
