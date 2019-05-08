package com.borealis.erates.supplier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.borealis.erates.supplier.exception.RatesProcessingException;

/**
 * @author Kastalski Sergey
 */
@Service
public class HttpTransport {
	
	private final static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public String sendPost(final String url) throws RatesProcessingException {
		return sendPost(url, null);
	}
	
	public String sendPost(final String url, final String body) throws RatesProcessingException {
		final HttpPost http = new HttpPost(url);
		if (body != null) {
			http.setEntity(new StringEntity(body, Charset.forName("UTF-8")));
		}
		return sendRequest(http);
	}
	
	public String sendGet(final String url) throws RatesProcessingException {
		return sendRequest(new HttpGet(url));
	}
	
	private String sendRequest(final HttpUriRequest http) throws RatesProcessingException {
		try (final CloseableHttpResponse response = httpclient.execute(http)) {
			final InputStream contentStream = response.getEntity().getContent();
			return IOUtils.toString(contentStream, Charset.forName("UTF-8"));
		} catch (final IOException e) {
			throw new RatesProcessingException("Failed http-request", e);
		}
	}
	
}
