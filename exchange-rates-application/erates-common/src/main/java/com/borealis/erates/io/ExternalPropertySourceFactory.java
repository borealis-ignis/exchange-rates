package com.borealis.erates.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * @author Kastalski Sergey
 */
public class ExternalPropertySourceFactory implements PropertySourceFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ExternalPropertySourceFactory.class);
	
	
	private final static String[] commonPropertyFiles = { PropertyFiles.UPDATER_PROPERTIES, PropertyFiles.DB_PROPERTIES, PropertyFiles.ERATES_PROPERTIES };
	
	private final static String SLASH = File.separator;
	
	private final static String PAHT_TO_APPLICATION = System.getProperty("user.dir");
	
	private final static String BANKS_FOLDER = "banks";
	
	private final static String CONF_FOLDER = "conf";
	
	@Override
	public PropertySource<?> createPropertySource(final String name, final EncodedResource internalResource) throws IOException {
		if (internalResource == null || internalResource.getResource() == null) {
			throw new IOException("Internal properties file is not found");
		}
		
		final String propertyFileName = internalResource.getResource().getFilename();
		if (StringUtils.isBlank(propertyFileName)) {
			throw new IOException("Internal properties file name is not found");
		}
		
		// properties for banks should be located in 'banks' directory (conf/banks/)
		final String banksPropertiesPath = (ArrayUtils.contains(commonPropertyFiles, propertyFileName))? "" : BANKS_FOLDER;
		final File fileResource = new File(PAHT_TO_APPLICATION + SLASH + CONF_FOLDER + SLASH + banksPropertiesPath + SLASH + propertyFileName);
		
		final EncodedResource resource;
		if (fileResource.exists()) {
			resource = new EncodedResource(new FileSystemResource(fileResource), Charset.forName("UTF-8"));
		} else {
			resource = internalResource;
		}
		
		logger.info("Loaded properties file: " + resource.getResource().getFile().getAbsolutePath());
		
		return (name != null ? new ResourcePropertySource(name, resource) : new ResourcePropertySource(resource));
	}
	
}
