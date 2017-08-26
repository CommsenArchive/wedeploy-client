
# Unofficial Java client for WeDeploy 

This is client library for Java applications that need to talk to [WeDeploy](http://wedeploy.com) services!

## What it does?

It provides simple way to interact with [WeDeploy](http://wedeploy.com) services by hiding the remote calls behind convenient to use local APIs. 

## Why another Java client ?

When this project started the Java client provided by [WeDeploy](http://wedeploy.com) had several issues. The major two ware
	- the API was modeled after the JaavaScript API which makes it hard to work with in Java
	- it wasn't modular which makes it hard to us it in OSGi runtimes
	 
 This client is composed of several modules prepared to be deployed in OSGi container. For traditional Java applications modules are loaded via standard [Java SPI](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).

## Modules

### `com.commsen.wedeploy.client` 

Provides API contracts (in the form of interfaces and data classes) for all supported WeDeploy client services. This is the module that provides the types your application will use.

### `com.commsen.wedeploy.client.api`

Client for core WeDeploy services. Allows to:

 - check WeDeploy's status (see [StatusServiceTest](tests/src/test/java/com/commsen/wedeploy/client/api/test/StatusServiceTest.java) for usage exmple)

### `com.commsen.wedeploy.client.data`

Client for core WeDeploy's `DATA` services. Allows to:

 - create / remove / update collections (see [WeDeployCollectionTest](tests/src/test/java/com/commsen/wedeploy/client/data/test/WeDeployCollectionTest.java) for usage exmple)
 - create / remove / update documents (see [WeDeployDocumentTest](tests/src/test/java/com/commsen/wedeploy/client/data/test/WeDeployDocumentTest.java) for usage exmple)
 - use Java beans or maps as documents

### `com.commsen.wedeploy.transport.rest.okhttp`

[OkHttp](http://square.github.io/okhttp/) based implementation of the rest transport.

### `wedeploy-client-bom`

BOM (Bill of Material) used by [Eccentric Modularity](https://github.com/azzazzel/EM) to auto provision applications and build standalone executable! 

## Usage

 - Standard classpath applications - make sure to have `com.commsen.wedeploy.client` and the jar files of needed modules on classpath.  
 - OSGi containers - provision with `com.commsen.wedeploy.client` and whichever other modules you use

If you are building Java (SpringBoot like) microservices to be deployed on WeDeploy - consider [Eccentric Modularity](https://github.com/azzazzel/EM)!  

Have a look at the [source code of `tests` module](tests/src/test/java/com/commsen/wedeploy/client) for examples of using the APIs!

## License

The project is released under [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
