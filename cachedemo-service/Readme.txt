Purpose
=======
A "cachedemonew" project using the common library  ("common-code") for caching behaviour.


--The common library is part of "common-code" project.

--It currently uses a "ConcurrentMapCacheManager" as the strategy for CacheManager.

--It is designed to be pluggable so that cacheManager can be changed to use another custom Cache Manager like SpringCacheManager
  without impacting the client.




The "cachedemonew" project exposes 2 API Endpoints
===================================================
1./getQuotes and 
2./getBooks


The Caching behaviour is controlled by means of configuration in a repository folder managed by Spring Cloud Config Service
using the "native" profile.

The repository folder is in "/hello-repo".

It has the application configuration file (cache-demo.yml) for the cachedemonew service.

The application configuration contains the below properties.
===========================================================

1.cacheEnabled: true    

This property is to switch on/off Caching behaviour to a service as required either at startup and/or at runtime.
When true caching behaviour will be turned on for the entire service, otherwise it will be turned off.

2.caches: quotes,books
	
The List of cache names used by the service.

3. quotesCacheEnabled: true
	
The property which determines if the /getQuotes API response should be cached or not.

When "true" caching behaviour will be turned on for the /getQuotes API endpoint which used the "quotes" cache ,
otherwise it will be turned off.

4.booksCacheEnabled: true   

The property which determines if the /getBooks API response should be cached or not.

When "true" caching behaviour will be turned on for the /getBooks API endpoint which used the "books" cache ,
otherwise it will be turned off.
