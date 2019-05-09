package com.borealis.erates;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

/**
 * @author Kastalski Sergey
 */
public abstract class TestIOUtil {
	
	public static String readFile(final String path) {
		try {
			return IOUtils.toString(TestIOUtil.class.getClassLoader().getResourceAsStream(path), Charset.forName("UTF-8"));
		} catch (final Exception e) {
			throw new AssertionError("Can't read file '" + path + "'", e);
		}
	}
	
}
