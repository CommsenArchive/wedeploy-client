package com.commsen.wedeploy.client.data.impl;

import static com.commsen.wedeploy.client.WeDeployConstants.MASTER_TOKEN;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.CollectionDTO;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataException;
import com.commsen.wedeploy.client.data.WeDeployDataMapper;
import com.commsen.wedeploy.client.data.WeDeployDataStorage;
import com.commsen.wedeploy.transport.rest.WeDeployResponse;
import com.commsen.wedeploy.transport.rest.WeDeployRestClient;

public class SimpleWeDeployDataStorage implements WeDeployDataStorage {

	Logger log = LoggerFactory.getLogger(SimpleWeDeployDataStorage.class);

	URI baseURI;

	Map<String, String> baseHeaders = new HashMap<String, String>();

	WeDeployRestClient restClient;

	WeDeployDataMapper jsonMapper;

	public SimpleWeDeployDataStorage(String project, String service, Map<String, Object> properties,
			WeDeployRestClient restClient, WeDeployDataMapper jsonMapper) {

		try {
			this.baseURI = new URI("https", null, service + "-" + project + ".wedeploy.io", -1, null, null, null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(
					"Failed to construct URI from project '" + project + "' and service '" + service + "'!");
		}

		this.restClient = restClient;
		this.jsonMapper = jsonMapper;

		baseHeaders.put("Content-Type", "application/json");
		if (properties != null && properties.get(MASTER_TOKEN) != null) {
			baseHeaders.put("Authorization", "Bearer " + properties.get(MASTER_TOKEN));
		}
		
	}

	public List<CollectionDTO> getCollections() throws WeDeployClientException {

		log.debug("Getting all collections");

		WeDeployResponse response;
		try {
			response = restClient.get(this.baseURI, baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collections!", e);
		}

		log.debug("Response: " + response);

		return jsonMapper.listFromJson(response.getOKBody(), CollectionDTO.class);
	}

	public Optional<CollectionDTO> getCollection(String name) throws WeDeployClientException {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Illegal collection name: " + name);
		}

		log.debug("Getting collection " + name);

		URI collectionURL = URI.create(baseURI.toString() + "?name=" + name);

		WeDeployResponse response;
		try {
			response = restClient.get(collectionURL, baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to get collection ", e);
		}

		log.debug("Response: " + response);

		if (404 == response.getCode()) {
			return Optional.empty();
		}

		return Optional.of(jsonMapper.fromJson(response.getOKBody(), CollectionDTO.class));
	}

	public CollectionDTO getOrCreateCollection(String name) throws WeDeployClientException {
		Optional<CollectionDTO> collectionDTO = getCollection(name);
		if (collectionDTO.isPresent()) {
			return collectionDTO.get();
		} else {
			return createCollection(name);
		}
	}

	public CollectionDTO createCollection(String name) throws WeDeployClientException {
		return createCollection(CollectionDTO.from(name));
	}

	public CollectionDTO createCollection(String name, Map<String, Object> mappings) throws WeDeployClientException {
		return createCollection(CollectionDTO.from(name, mappings));

	}

	public CollectionDTO createCollection(CollectionDTO collectionDTO) throws WeDeployClientException {

		collectionDTO = collectionDTO.validated();

		log.debug("Creating collection " + collectionDTO);

		WeDeployResponse response;
		try {
			response = restClient.post(this.baseURI, baseHeaders, jsonMapper.toJson(collectionDTO));
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to create collection '" + collectionDTO + "'!", e);
		}

		log.debug("Response: " + response);

		return jsonMapper.fromJson(response.getOKBody(), CollectionDTO.class);
	}

	public void deleteCollection(String name) throws WeDeployClientException {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Illegal collection name: " + name);
		}

		log.debug("Deleting collection " + name);

		URI deleteURL = URI.create(baseURI.toString() + "/" + name);
		try {
			restClient.delete(deleteURL, baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to delete collection '" + name + "'!", e);
		}

	}

	public void deleteCollection(CollectionDTO collectionDTO) throws WeDeployClientException {
		deleteCollection(collectionDTO.validated().name);
	}

	public void deleteCollections(List<CollectionDTO> collections) throws WeDeployClientException {
		for (CollectionDTO collectionDTO : collections) {
			collectionDTO.validated();
		}
		for (CollectionDTO collectionDTO : collections) {
			deleteCollection(collectionDTO);
		}
	}

	public void deleteCollections(CollectionDTO... collections) throws WeDeployClientException {
		for (int i = 0; i < collections.length; i++) {
			CollectionDTO collectionDTO = collections[i];
			collectionDTO.validated();
		}
		for (int i = 0; i < collections.length; i++) {
			CollectionDTO collectionDTO = collections[i];
			deleteCollection(collectionDTO);
		}
	}

	public void deleteCollectionsByName(String... collections) throws WeDeployClientException {
		for (int i = 0; i < collections.length; i++) {
			String name = collections[i];
			if (name == null || name.trim().isEmpty()) {
				throw new IllegalArgumentException("Illegal collection name: " + name);
			}
		}
		for (int i = 0; i < collections.length; i++) {
			String name = collections[i];
			deleteCollection(name);
		}

	}

	public void deleteCollectionsByName(List<String> collections) throws WeDeployClientException {
		for (String name : collections) {
			if (name == null || name.trim().isEmpty()) {
				throw new IllegalArgumentException("Illegal collection name: " + name);
			}
		}
		for (String name : collections) {
			deleteCollection(name);
		}
	}

	public boolean collectionExists(String name) throws WeDeployClientException {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Illegal collection name: " + name);
		}

		return getCollection(name).isPresent();

	}

	public boolean collectionExists(CollectionDTO collectionDTO) throws WeDeployClientException {
		return getCollection(collectionDTO.validated().name).isPresent();
	}

	public CollectionDTO updateMappings(String name, Map<String, Object> mappings) throws WeDeployClientException {
		return updateMappings(CollectionDTO.from(name, mappings));
	}

	public CollectionDTO updateMappings(CollectionDTO collectionDTO) throws WeDeployClientException {

		log.debug("Updating mapping for " + collectionDTO);

		WeDeployResponse response;
		try {
			restClient.call(this.baseURI, "PATCH", baseHeaders, jsonMapper.toJson(collectionDTO));
			response = restClient.get(URI.create(this.baseURI + "?name=" + collectionDTO.name), baseHeaders);
		} catch (IOException e) {
			throw new WeDeployDataException("Failed to create collection " + collectionDTO, e);
		}

		log.debug("Response: " + response);

		return jsonMapper.fromJson(response.getOKBody(), CollectionDTO.class);
	}


	public WeDeployDataCollection collection(String name) {
		return new SimpleWeDeployDataCollection(this, name);
	}

}
