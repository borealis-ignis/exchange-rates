package com.borealis.erates.supplier.prior.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Kastalski Sergey
 */
@Configuration
@PropertySources({
	@PropertySource(value="classpath:priorb.properties", ignoreResourceNotFound=true)
})
public class PriorConfiguration {

}
