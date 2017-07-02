package com.demo.autoconfigure.config.cache;

public class CacheExpiryPolicy {

	String type;
	long duration;
	String timeUnit;
	
	
	public long getDuration() {
		return duration;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public String getType() {
		return type;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "CacheExpiryPolicy [type=" + type + ", duration=" + duration + ", timeUnit=" + timeUnit + "]";
	}
}