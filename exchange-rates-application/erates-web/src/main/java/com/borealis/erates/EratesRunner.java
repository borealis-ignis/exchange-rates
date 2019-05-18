package com.borealis.erates;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * @author Kastalski Sergey
 */
@Component
public class EratesRunner implements ApplicationRunner {
	
	private String log4j2Path;
	
	public EratesRunner(@Value("${log4j.path:}") final String log4j2Path) {
		this.log4j2Path = log4j2Path;
	}
	
	@Override
	public void run(final ApplicationArguments args) throws Exception {
		final LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(obtainURI());
	}
	
	private URI obtainURI() throws URISyntaxException {
		if (StringUtils.isBlank(log4j2Path)) {
			log4j2Path = "classpath:log4j2.xml";
		}
		
		if (log4j2Path.startsWith("classpath")) {
			return ResourceUtils.toURI(log4j2Path);
		}
		return new File(log4j2Path).toURI();
	}
	
}
