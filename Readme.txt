Purpose
=======

This repository consists of the below projects/folders.

(1) cachedemo-service
	A simple Spring Boot based project exposing 2 API Endpoints and having
	a dependency on "common-new" library for caching behaviour.
	
	When "Caching" is turned on, the responses of its API Endpoints will be cached using 
	the cacheManager used by the "common-new" library.
	
	The "cacheEnabled" property is used to determine if Caching behaviour is turned on or not 
	for the Service. it is managed by Spring Cloud Config Service using a native profile in a file system folder.
	
	It also uses 2 more properties, "quotesCacheEnabled" and "booksCacheEnabled" to determine if the caching behaviour
	should be allowed or not for a specific endpoint.
	
	Each of these properties are used by "/getQuotes" and "/getBooks" API Endpoints of the service to store
	in their respective caches.

2.caching-component
	
	The "caching-component" is a library project which is used to add caching behaviour to a service.
	
	When added as a dependency to a service, it provides caching behaviour based on if "caching" is turned on or not
	for the service.
	
	It checks once at atsrtup and later at runtime whenever the configured value is updated in repository managed
	by Spring Cloud Config Service.


3.configuration-service and

	The demo Spring Cloud Config based service which manages the applications configuration.
	
	It uses the native profile by using a filesystem folder to managed the configurations.
	

4.hello-repo
	
	The filesystem based repository which is managed by configuration-service.
	
	It only has the "cache-demo.yml" which is the application configuration file for "cachedemo-service".