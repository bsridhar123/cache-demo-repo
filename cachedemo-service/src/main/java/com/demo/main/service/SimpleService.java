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

	@Value("${customerCacheEnabled}")
	private boolean customerCacheEnabled;

	public boolean getCustomerCacheEnabled() {
		logger.info("customerCacheEnabled" + customerCacheEnabled);
		return customerCacheEnabled;
	}

	@Cacheable(value = "employee")
	public String getByEmployeeNo(String empNo) {
		logger.info("Inside getByEmployeeNo in SimpleService");
		simulateSlowService();
		return "Employee:" + empNo;
	}

	@Cacheable(value = "department")
	public String getByDepartmentNo(String departmentNo) {
		logger.info("Inside getByDepartmentNo in SimpleService");
		simulateSlowService();
		return "Department:" + departmentNo;
	}
	@Cacheable(value = "customer", condition = "#root.target.customersCacheEnabled==true")
	public String getCustomerByCustomerNo(String customerNo) {
		logger.info("Inside getCustomerByCustomerNo in SimpleService");
		simulateSlowService();
		return "Customer:" + customerNo;
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
