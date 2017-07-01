Purpose
=======

This project is for a "caching-component" library.


--It currently uses a "ConcurrentMapCacheManager" as the strategy for CacheManager.

--It is designed to be pluggable so that cacheManager can be changed to use another custom Cache Manager like SpringCacheManager
  without impacting the client.


When added as a dependency to the "cachedemo-service" project, it uses the "cacheEnabled" property to
determine if the caching behaviour should be applied or not.

When "cacheEnabled" is true, it uses a "ConcurrentMapCacheManager".
When "cacheEnabled" is false, it uses "NoOpCacheManager".

The "cacheEnabled" is part of the application configuration which will be in externalized in a repository folder 
managed by Spring Cloud Config Service.

