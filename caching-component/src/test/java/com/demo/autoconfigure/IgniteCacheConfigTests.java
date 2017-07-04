package com.demo.autoconfigure;

import static org.junit.Assert.assertEquals;

import org.apache.ignite.cache.spring.SpringCacheManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.autoconfigure.config.cache.CacheConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CacheConfig.class)
public class IgniteCacheConfigTests {

	@Autowired
	CacheConfig cacheConfig;
	
	 
	@Test
	public void testSpringCacheManager() {
	
		IgniteCacheConfig obj=new IgniteCacheConfig();
		cacheConfig.setCacheEnabled(true);
		obj.setCacheConfig(cacheConfig);
		assertEquals(obj.cacheManager().getClass(),SpringCacheManager.class);
	}

	@Test
	public void testNoOpsCacheManager() {
		IgniteCacheConfig obj=new IgniteCacheConfig();
		cacheConfig.setCacheEnabled(false);
		obj.setCacheConfig(cacheConfig);
		assertEquals(obj.cacheManager().getClass(),NoOpCacheManager.class);
	}

}
