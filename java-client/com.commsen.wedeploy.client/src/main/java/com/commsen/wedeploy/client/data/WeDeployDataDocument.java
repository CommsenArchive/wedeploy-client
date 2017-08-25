package com.commsen.wedeploy.client.data;

public class WeDeployDataDocument<T> {

	private T object;
	
	private String id;

	public WeDeployDataDocument() {
		// no-arg constructor for gson
	}

	public WeDeployDataDocument(String id, T object) {
		this.id = id;
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public String getId() {
		return id;
	}
	
}
