package com.borealis.erates.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EratesPageController {
	
	@GetMapping(path="/erates")
	public String getSingleRecipePage(
			final Map<String, Object> model,
			@RequestParam(required = false, name = "bankid") final Long bankId) {
		
		return "erates";
	}
	
}
