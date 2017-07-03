package com.demo.autoconfigure;



import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.cache.configuration.Factory;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.AbstractEvictionPolicy;
import org.apache.ignite.cache.eviction.EvictionPolicy;
import org.apache.ignite.cache.eviction.fifo.FifoEvictionPolicy;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.cache.eviction.sorted.SortedEvictionPolicy;
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
import com.demo.autoconfigure.config.cache.CacheEvictionPolicy;
import com.demo.autoconfigure.config.cache.CacheExpiryPolicy;

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
			
			//List<CacheBean> cacheBeanList=cacheConfig.getCaches();
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
				cacheConfigList.add(getConfigurationForCache(caches.get(i)));
			}
		}
		LOGGER.info("Leaving");
		return cacheConfigList.toArray(new CacheConfiguration[cacheConfigList.size()]);

	}

	private CacheConfiguration<?, ?> getConfigurationForCache(CacheBean cacheBean) {
		LOGGER.info("Entering");
		CacheConfiguration<?, ?> cacheConfigurationInstance = new CacheConfiguration<Object, Object>();
		LOGGER.info("Creating Ignite Cache Configuration for Cache :" + cacheBean.getName());
		cacheConfigurationInstance.setName(cacheBean.getName());
		String cacheMode=cacheBean.getCacheMode();
		if(null!=cacheMode && cacheMode.indexOf("PAR")>0){
			cacheConfigurationInstance.setCacheMode(CacheMode.PARTITIONED);	
		}else if(null!=cacheMode && cacheMode.indexOf("REP")>0){
			cacheConfigurationInstance.setCacheMode(CacheMode.REPLICATED);	
		}else if(null!=cacheMode && cacheMode.indexOf("LOC")>0){
			cacheConfigurationInstance.setCacheMode(CacheMode.LOCAL);
		}else{
			cacheConfigurationInstance.setCacheMode(CacheMode.REPLICATED);
		}
		
		cacheConfigurationInstance.setBackups(1);
		cacheConfigurationInstance.setExpiryPolicyFactory(getExpiryPolicy(cacheBean.getExpiryPolicy()));
		cacheConfigurationInstance.setEvictionPolicy(getEvictionPolicy(cacheBean.getEvictionPolicy()));
		
		LOGGER.info("Creating Ignite Cache Configuration for Cache :" + cacheBean.getName());
		LOGGER.info("Leaving");
		return cacheConfigurationInstance;
	}
	
	

	private EvictionPolicy<?, ?> getEvictionPolicy(CacheEvictionPolicy evictionPolicy) {
		String type=evictionPolicy.getType();
		AbstractEvictionPolicy<?, ?> policy=null;
		if(null!=type && type.equalsIgnoreCase("FIFO")){
			policy=new FifoEvictionPolicy<Object, Object>();
		}else if(null!=type && type.equalsIgnoreCase("LRU")){
			policy=new LruEvictionPolicy<Object, Object>();
		}else if(null!=type && type.equalsIgnoreCase("RANDOM")){
			policy=new SortedEvictionPolicy<Object, Object>();
		}
		policy.setMaxSize(Integer.parseInt(evictionPolicy.getMaxSize()));
		return policy;
	}

	private Factory<? extends ExpiryPolicy>  getExpiryPolicy(CacheExpiryPolicy expiryPolicy) { 
		
		String type=expiryPolicy.getType();
		String strTimeUnit=expiryPolicy.getTimeUnit();
		long duration=expiryPolicy.getDuration();
		if(null!=type && type.equalsIgnoreCase("CREATION_TIME")) {
			if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("MINUTES")){
				return CreatedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
				//return new CreatedExpiryPolicy(new Duration(MINUTES, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("HOURS")){
				return CreatedExpiryPolicy.factoryOf(new Duration(HOURS, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("SECONDS")){
				return CreatedExpiryPolicy.factoryOf(new Duration(SECONDS, duration));
			}else{
				return CreatedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}
		}else if(null!=type && type.equalsIgnoreCase("LAST_ACCESSED_TIME")) {
			if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("MINUTES")){
				return AccessedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("HOURS")){
				return AccessedExpiryPolicy.factoryOf(new Duration(HOURS, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("SECONDS")){
				return AccessedExpiryPolicy.factoryOf(new Duration(SECONDS, duration));
			}else{
				return AccessedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}
		}else if(null!=type && type.equalsIgnoreCase("MODIFIED_TIME")) {
			if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("MINUTES")){
				return ModifiedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("HOURS")){
				return ModifiedExpiryPolicy.factoryOf(new Duration(HOURS, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("SECONDS")){
				return ModifiedExpiryPolicy.factoryOf(new Duration(SECONDS, duration));
			}else{
				return ModifiedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}
		}else{
			if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("MINUTES")){
				return CreatedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("HOURS")){
				return CreatedExpiryPolicy.factoryOf(new Duration(HOURS, duration));
			}else if(null!=strTimeUnit && strTimeUnit.equalsIgnoreCase("SECONDS")){
				return CreatedExpiryPolicy.factoryOf(new Duration(SECONDS, duration));
			}else{
				return CreatedExpiryPolicy.factoryOf(new Duration(MINUTES, duration));
			}
		}
		
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
