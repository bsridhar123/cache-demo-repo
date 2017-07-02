package com.demo.autoconfigure.config.cache;



public class CacheBean {

	String name;
	String cacheMode;

	CacheExpiryPolicy expiryPolicy;
	CacheEvictionPolicy evictionPolicy;

	
	@Override
	public String toString() {
		return "CacheBean [name=" + name + ", cacheMode=" + cacheMode + ", expiryPolicy=" + expiryPolicy
				+ ", evictionPolicy=" + evictionPolicy + "]";
	}

	public String getCacheMode() {
		return cacheMode;
	}

	public CacheEvictionPolicy getEvictionPolicy() {
		return evictionPolicy;
	}

	public CacheExpiryPolicy getExpiryPolicy() {
		return expiryPolicy;
	}

	public String getName() {
		return name;
	}

	

	public void setCacheMode(String cacheMode) {
		this.cacheMode = cacheMode;
	}

	public void setEvictionPolicy(CacheEvictionPolicy evictionPolicy) {
		this.evictionPolicy = evictionPolicy;
	}

	public void setExpiryPolicy(CacheExpiryPolicy expiryPolicy) {
		this.expiryPolicy = expiryPolicy;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}