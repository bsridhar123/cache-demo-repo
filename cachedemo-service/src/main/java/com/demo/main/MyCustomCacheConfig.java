package com.demo.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
@EnableCaching
@RefreshScope
@AutoConfigureBefore(CacheAutoConfiguration.class)
public class MyCustomCacheConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyCustomCacheConfig.class);

	/* Flag to Determine if Cache should be Enabled or not for the service */
	@Value("${cacheEnabled}")
	private boolean cacheEnabled;

	/* Name of the caches to be created */
	@Value("${caches}")
	private String caches;

	@Bean
	@Primary
	@RefreshScope
	CacheManager cacheManager() {

		// If Cache is Enabled then swap the cacheManager for
		// ConcurrentMapCacheManager
		if (cacheEnabled) {
			LOGGER.info("Cache is ENABLED...");
			LOGGER.info("Plugging-in SimpleCacheManager...");
			List<String> cacheNameList = Arrays.asList(caches.split(","));
			LOGGER.info("cacheNameList...:" + cacheNameList.toString());
			SimpleCacheManager cacheManager = new SimpleCacheManager();

			List<ConcurrentMapCache> conMapList = new ArrayList<>();
			for (int i = 0; i < cacheNameList.size(); i++) {
				conMapList.add(new ConcurrentMapCache(cacheNameList.get(i)));
			}

			cacheManager.setCaches(conMapList);

			return cacheManager;
		} else {
			LOGGER.info("Cache is DISABLED...");
			LOGGER.info("Plugging-in NoOpCacheManager...");
			CacheManager cacheManager = new NoOpCacheManager();
			return cacheManager;
		}

	}

}
