package com.commsen.wedeploy.client.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.commsen.wedeploy.client.WeDeployClientException;

public interface WeDeployDataStorage {

	WeDeployDataCollection collection(String name);
	
	List<CollectionDTO> getCollections() throws WeDeployClientException;

	Optional<CollectionDTO> getCollection(String name) throws WeDeployClientException;

	boolean collectionExists(String name) throws WeDeployClientException;
	
	boolean collectionExists(CollectionDTO collectionDTO) throws WeDeployClientException;
	
	CollectionDTO getOrCreateCollection(String name) throws WeDeployClientException;

	CollectionDTO createCollection(String name) throws WeDeployClientException;

	CollectionDTO createCollection(String name, Map<String, Object> mappings) throws WeDeployClientException;

	CollectionDTO createCollection(CollectionDTO collectionDTO) throws WeDeployClientException;

	void deleteCollection(String name) throws WeDeployClientException;

	void deleteCollection(CollectionDTO collection) throws WeDeployClientException;

	void deleteCollections(List<CollectionDTO> collections) throws WeDeployClientException;

	void deleteCollections(CollectionDTO... colelctions) throws WeDeployClientException;

	void deleteCollectionsByName(List<String> collections) throws WeDeployClientException;

	void deleteCollectionsByName(String... collections) throws WeDeployClientException;

	CollectionDTO updateMappings (String name, Map<String, Object> mappings) throws WeDeployClientException;

	CollectionDTO updateMappings (CollectionDTO collectionDTO) throws WeDeployClientException;

}