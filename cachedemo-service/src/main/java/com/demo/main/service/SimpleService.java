package com.demo.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class SimpleService {

	private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

	@Value("${booksCacheEnabled}")
	private boolean booksCacheEnabled;

	public boolean getBooksCacheEnabled() {
		logger.info("booksCacheEnabled" + booksCacheEnabled);
		return booksCacheEnabled;
	}

	@Cacheable(value = "quotes")
	public String getByQuoteRefNo(String isbn) {
		logger.info("Inside getByQuoteRefNo in SimpleBookRepository");
		simulateSlowService();
		return "Quote:" + isbn;
	}

	@Cacheable(value = "books", condition = "#root.target.booksCacheEnabled==true")
	public String getBookByNo(String bookno) {
		logger.info("Inside getBookByNo in SimpleBookRepository");
		simulateSlowService();
		return "Book:" + bookno;
	}

	private void simulateSlowService() {
		try {
			long time = 3000L;
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

}
