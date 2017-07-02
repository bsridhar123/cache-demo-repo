package com.demo.autoconfigure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.autoconfigure.config.cache.CacheBean;
import com.demo.autoconfigure.config.cache.CacheConfig;


@Configuration
@EnableCaching
@RefreshScope
public class IgniteCacheConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(IgniteCacheConfig.class);

	@Autowired
	CacheConfig cacheConfig;

	@Bean
	@RefreshScope
	CacheManager cacheManager() {

		LOGGER.info("cacheconfig:: " + cacheConfig);
		// If Cache is Enabled then swap the cacheManager for
		// SpringCacheManager instance for Apache Ignite
		if (cacheConfig.isCacheEnabled()) {
			LOGGER.info("Cache is ENABLED...");
			LOGGER.info("Plugging-in SpringCacheManager for Apache Ignite...");

			// Stop Ignite Instance if already running --TODO: Ideally should
			// check if Ignite Instance is started and if so STOP it.
			LOGGER.info("Stopping Ignite Instance, if running ");
			Ignition.stop(true);

			SpringCacheManager cacheManager = new SpringCacheManager();

			LOGGER.info("Creating IgniteConfiguration instance...");
			IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

			LOGGER.info("Ignite Client Mode...:" + cacheConfig.isClientMode());
			igniteConfiguration.setClientMode(cacheConfig.isClientMode());

			LOGGER.info("Setting Cache Configuration to Apache Ignite Configuration...");
			igniteConfiguration.setCacheConfiguration(getAppCaches());
			LOGGER.info("Setting Ignite Configuration to CacheManager instance...");
			
			if(null!=cacheConfig.getAddresses()){
				LOGGER.info("Configured Address List is: " +cacheConfig.getAddresses() );
				LOGGER.info("Setting DiscoverySpi to IgniteConfiguration..." );
				igniteConfiguration.setDiscoverySpi(doClusterSetup(cacheConfig.getAddresses()));
			}
			cacheManager.setConfiguration(igniteConfiguration);
			LOGGER.info("Leaving");
			return cacheManager;

		} else {
			// Stop Ignite Instance if already running TODO: Ideally should
			// allow it to run...
			LOGGER.info("Stopping Ignite Instance, if running ");
			Ignition.stop(true);
			LOGGER.info("Cache is DISABLED...");
			LOGGER.info("Plugging-in NoOpCacheManager...");
			CacheManager cacheManager = new NoOpCacheManager();
			LOGGER.info("Leaving");
			return cacheManager;
		}

	}

	private CacheConfiguration<?, ?>[] getAppCaches() {
		LOGGER.info("Entering");
		List<CacheBean> caches = cacheConfig.getCaches();
		List<CacheConfiguration<?, ?>> cacheConfigList = new ArrayList<>();
		if (null != caches) {
			for (int i = 0; i < caches.size(); i++) {
				cacheConfigList.add(getConfigurationForCache(caches.get(i).getName()));
			}
		}
		LOGGER.info("Leaving");
		return cacheConfigList.toArray(new CacheConfiguration[cacheConfigList.size()]);

	}

	private CacheConfiguration<?, ?> getConfigurationForCache(String cacheName) {
		LOGGER.info("Entering");
		CacheConfiguration<?, ?> cacheConfigurationInstance = new CacheConfiguration<Object, Object>();
		LOGGER.info("Creating Ignite Cache Configuration for Cache :" + cacheName);
		cacheConfigurationInstance.setName(cacheName);
		cacheConfigurationInstance.setCacheMode(CacheMode.PARTITIONED);
		cacheConfigurationInstance.setBackups(1);
		LOGGER.info("Creating Ignite Cache Configuration for Cache :" + cacheName);
		LOGGER.info("Leaving");
		return cacheConfigurationInstance;
	}
	
	private TcpDiscoverySpi doClusterSetup(Collection<String> addresses){
		LOGGER.info("Entering");
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		 
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		 
		ipFinder.setAddresses(addresses);
		
		
		 
		spi.setIpFinder(ipFinder);
		LOGGER.info("Leaving");
		
		return spi;
		 
	}
	
	
}
