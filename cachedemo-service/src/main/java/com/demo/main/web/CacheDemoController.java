package com.demo.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.main.service.SimpleService;

@RestController
public class CacheDemoController {
	private static final Logger logger = LoggerFactory.getLogger(CacheDemoController.class);

	private final SimpleService simpleService;

	public CacheDemoController(SimpleService simpleService) {
		this.simpleService = simpleService;

	}

	
	@GetMapping("/getQuotes")
	public void getQuotes(){
		logger.info("quote-12 -->" + simpleService.getByQuoteRefNo("12"));
		logger.info("quote-12 -->" + simpleService.getByQuoteRefNo("12"));
		logger.info("quote-22 -->" + simpleService.getByQuoteRefNo("22"));
		logger.info("quote-12 -->" + simpleService.getByQuoteRefNo("12"));
		logger.info("quote-12 -->" + simpleService.getByQuoteRefNo("12"));
		logger.info("quote-22 -->" + simpleService.getByQuoteRefNo("22"));
	}
	
	
	@GetMapping("/getBooks")
	public void getBooks(){
		logger.info("isbn-25 -->" + simpleService.getBookByNo("25"));
		logger.info("isbn-26 -->" + simpleService.getBookByNo("26"));
		logger.info("isbn-27 -->" + simpleService.getBookByNo("27"));
		logger.info("isbn-25 -->" + simpleService.getBookByNo("25"));
		logger.info("isbn-26 -->" + simpleService.getBookByNo("26"));
	}
	
}
