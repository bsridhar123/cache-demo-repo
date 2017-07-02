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

	
	@GetMapping("/employee")
	public void getByEmployeeNo(){
		logger.info("employee-12 -->" + simpleService.getByEmployeeNo("12"));
		logger.info("employee-12 -->" + simpleService.getByEmployeeNo("12"));
		logger.info("employee-22 -->" + simpleService.getByEmployeeNo("22"));
		logger.info("employee-12 -->" + simpleService.getByEmployeeNo("12"));
		logger.info("employee-12 -->" + simpleService.getByEmployeeNo("12"));
		logger.info("employee-22 -->" + simpleService.getByEmployeeNo("22"));
	}
	
	
	@GetMapping("/department")
	public void getMembersByNo(){
		logger.info("department-25 -->" + simpleService.getByDepartmentNo("25"));
		logger.info("department-26 -->" + simpleService.getByDepartmentNo("26"));
		logger.info("department-27 -->" + simpleService.getByDepartmentNo("27"));
		logger.info("department-25 -->" + simpleService.getByDepartmentNo("25"));
		logger.info("department-26 -->" + simpleService.getByDepartmentNo("26"));
	}
	
	@GetMapping("/customer")
	public void getCustomerByCustomerNo(){
		logger.info("customer-25 -->" + simpleService.getCustomerByCustomerNo("25"));
		logger.info("customer-26 -->" + simpleService.getCustomerByCustomerNo("26"));
		logger.info("customer-27 -->" + simpleService.getCustomerByCustomerNo("27"));
		logger.info("customer-25 -->" + simpleService.getCustomerByCustomerNo("25"));
		logger.info("customer-26 -->" + simpleService.getCustomerByCustomerNo("26"));
	}
}
