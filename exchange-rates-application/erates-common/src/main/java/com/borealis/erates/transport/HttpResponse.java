package com.borealis.erates.transport;

import org.apache.http.Header;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kastalski Sergey
 */
@Getter
@Setter
public class HttpResponse {
	
	private String body;
	
	private int status;
	
	private Header[] headers;
	
}
