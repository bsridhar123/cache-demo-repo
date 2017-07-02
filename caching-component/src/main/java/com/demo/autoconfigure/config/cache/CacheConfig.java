package com.demo.autoconfigure.config.cache;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "configuration")
@EnableConfigurationProperties
public class CacheConfig {

	private List<CacheBean> caches = new ArrayList<CacheBean>();
	private boolean clientMode;
	private boolean cacheEnabled;
	private Collection<String> addresses;
	public Collection<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(Collection<String> addresses) {
		this.addresses = addresses;
	}
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}
	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}
	public List<CacheBean> getCaches() {
		return caches;
	}
	@Override
	public String toString() {
		return "CacheConfig [caches=" + caches + ", clientMode=" + clientMode + "]";
	}
	public boolean isClientMode() {
		return clientMode;
	}
	public void setCaches(List<CacheBean> caches) {
		this.caches = caches;
	}
	public void setClientMode(boolean clientMode) {
		this.clientMode = clientMode;
	}
	
}