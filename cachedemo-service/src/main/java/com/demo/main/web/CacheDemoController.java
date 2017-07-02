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

	
	@GetMapping("/enrollment")
	public void getByEnrollmentNo(){
		logger.info("enrollment-12 -->" + simpleService.getByEnrollmentNo("12"));
		logger.info("enrollment-12 -->" + simpleService.getByEnrollmentNo("12"));
		logger.info("enrollment-22 -->" + simpleService.getByEnrollmentNo("22"));
		logger.info("enrollment-12 -->" + simpleService.getByEnrollmentNo("12"));
		logger.info("enrollment-12 -->" + simpleService.getByEnrollmentNo("12"));
		logger.info("enrollment-22 -->" + simpleService.getByEnrollmentNo("22"));
	}
	
	
	@GetMapping("/member")
	public void getMembersByNo(){
		logger.info("member-25 -->" + simpleService.getMembersByNo("25"));
		logger.info("member-26 -->" + simpleService.getMembersByNo("26"));
		logger.info("member-27 -->" + simpleService.getMembersByNo("27"));
		logger.info("member-25 -->" + simpleService.getMembersByNo("25"));
		logger.info("member-26 -->" + simpleService.getMembersByNo("26"));
	}
	
	@GetMapping("/plan")
	public void getByPlanNo(){
		logger.info("plan-25 -->" + simpleService.getByPlanNo("25"));
		logger.info("plan-26 -->" + simpleService.getByPlanNo("26"));
		logger.info("plan-27 -->" + simpleService.getByPlanNo("27"));
		logger.info("plan-25 -->" + simpleService.getByPlanNo("25"));
		logger.info("plan-26 -->" + simpleService.getByPlanNo("26"));
	}
}
