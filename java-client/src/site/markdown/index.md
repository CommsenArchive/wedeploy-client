
[<img style="float:right; margin:20px; width:200px;" src="images/wedeploy-logo.png" />](https://wedeploy.com)

This is unofficial Java client library for WeDeploy! 

[WeDeploy](https://wedeploy.com) provides a set of ready-to-use services that enables you to store data in the cloud, search and stream content in real time, authenticate users, send e-mails to your users and so much more!

This client allows Java applications to talk to WeDeploy cloud services as if those were local ones.

### Why unofficial Java client ?

When this project started the Java client provided by [WeDeploy](https://wedeploy.com) had several issues. The major two ware

 - the API was modeled after the JavaScript API which makes it hard to work with in Java
 - it wasn't modular which makes it hard to us it in modular runtimes (such as OSGi)
	 
This client is composed of several modules prepared to be deployed in OSGi container. For traditional Java applications modules are loaded via standard [Java SPI](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).

### Modules

<table>
	<tr>
		<th>API</th>
		<td>Provides API contracts (in the form of interfaces and data classes) for all supported WeDeploy client services. This is the module that provides the types your application will use.</td>
	</tr>
	<tr>
		<th>Cloud</th>
		<td>Client for (some of) WeDeploy's cloud services. It currently allows to:
			<ul>
				<li>check WeDeploy's status </li>
			</ul>
 		</td>
	</tr>
	<tr>
		<th>Data</th>
		<td>Client for WeDeploy's data services. It currently allows to:
			<ul>
			 	<li> create / remove / update collections </li>
 				<li> create / remove / update documents </li>
 				<li> use Java beans or maps as documents </li>
			</ul>
 		</td>
	</tr>
	<tr>
		<th>Gson data mapper</th>
		<td><a href="https://github.com/google/gson">Gson</a> based implementation of the data mapping contract consumed by data services.</td>
	</tr>
	<tr>
		<th>OkHttp transport</th>
		<td><a href="https://square.github.io/okhttp/">OkHttp</a> based implementation of the rest transport contract consumed by other services.</td>
	</tr>
	<tr>
		<th>BOM</th>
		<td>BOM (Bill of Material) used by provisioning tools (such us <a href="https://github.com/azzazzel/EM)">Eccentric Modularity</a> to auto provision applications and build stand-alone executables! </td>
	</tr>
</table>

 

### Usage

All modules are available in [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.commsen.wedeploy%22)!

 ------------ | ----------- |
| Traditional Java applications | make sure to have needed modules (API, cloud, data, mapper, transport, ...) in your classpath. |
| OSGi containers  | deploy needed modules (API, cloud, data, mapper, transport, …) in whatever way you container supports  |
| [Eccentric Modularity (EM)](https://github.com/azzazzel/EM) executable jar  | in your POM declare dependency on API module and import the BOM. EM will automatically provision required modules.  |

Here is simple example of `Data` service: 

	WeDeployClient weDeploy = new WeDeployClient();
	Collection collection = weDeploy.data().connect(project, service).collection("customers");
	
	List<WeDeployDataDocument<User>> loadedUsers = collection.read(User.class, 10, 10);
	User user = loadedUsers.get(1).getObject();
	String id = loadedUsers.get(1).getId();
	collection.update(id, updatedUser);
	collection.delete(id);
	collection.save(anotherUser);

In modular environment (OSGi and projects built with [Eccentric Modularity (EM)](https://github.com/azzazzel/EM)) you can simply inject the service 

	@Reference
	private WeDeployDataService weDeployDataService;
	
	...
	
	Collection collection = weDeployDataService.connect(project, service).collection("customers");
	
	...

### License

The project is released under [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
