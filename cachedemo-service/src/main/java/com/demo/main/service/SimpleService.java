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

	@Value("${membersCacheEnabled}")
	private boolean membersCacheEnabled;

	public boolean getMembersCacheEnabled() {
		logger.info("membersCacheEnabled" + membersCacheEnabled);
		return membersCacheEnabled;
	}

	@Cacheable(value = "enrollments")
	public String getByEnrollmentNo(String enrNo) {
		logger.info("Inside getByEnrollmentNo in SimpleService");
		simulateSlowService();
		return "Enrollment:" + enrNo;
	}

	@Cacheable(value = "plans")
	public String getByPlanNo(String planNo) {
		logger.info("Inside getByPlanNo in SimpleService");
		simulateSlowService();
		return "Plan:" + planNo;
	}
	@Cacheable(value = "members", condition = "#root.target.membersCacheEnabled==true")
	public String getMembersByNo(String memberNo) {
		logger.info("Inside getMembersByNo in SimpleService");
		simulateSlowService();
		return "Member:" + memberNo;
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
