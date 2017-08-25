package com.commsen.wedeploy.client.data.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.CollectionDTO;
import com.commsen.wedeploy.client.data.WeDeployDataException;

public class WeDeployCollectionTest extends BaseTest {

	public WeDeployCollectionTest() throws WeDeployClientException, IOException {
		super();
	}

	
	@Test
	public void invalidCollection() throws WeDeployClientException {
		CollectionDTO collectionDTO = new CollectionDTO();
		Assert.assertFalse(collectionDTO.isValid());
		collectionDTO.name="";
		Assert.assertFalse(collectionDTO.isValid());
		collectionDTO.name="   \t ";
		Assert.assertFalse(collectionDTO.isValid());
		try {
			collectionDTO.validated();
			Assert.fail("Failed to throw exception validating invalid collection");
		} catch ( WeDeployDataException e) {
		}
		collectionDTO.name="collection";
		Assert.assertTrue(collectionDTO.isValid());
	}
	
	@Test
	public void CRUD() throws WeDeployClientException {

		// clear all existing collections
		List<CollectionDTO> collections = dataStorageWithToken.getCollections();
		for (CollectionDTO collectionDTO : collections) {
			dataStorageWithToken.deleteCollection(collectionDTO);
		}

		// create one collection
		CollectionDTO collectionDTO = dataStorageWithToken.createCollection("test1");
		Assert.assertEquals("test1", collectionDTO.name);
		Assert.assertEquals(new HashMap<String, String>(), collectionDTO.mappings);
		Assert.assertTrue(collectionDTO.size > 0);
		Assert.assertTrue(collectionDTO.documents == 0);

		Assert.assertTrue(dataStorageWithToken.collectionExists("test1"));
		Assert.assertTrue(dataStorageWithToken.collectionExists(collectionDTO));

		collections = dataStorageWithToken.getCollections();
		Assert.assertTrue(collections.size() == 1);

		// create one collection with some mappings
		HashMap<String, Object> mappings = new HashMap<String, Object>();
		mappings.put("name", "string");
		mappings.put("count", "integer");
		collectionDTO = dataStorageWithToken.createCollection("test2", mappings);
		Assert.assertEquals("test2", collectionDTO.name);
		Assert.assertEquals(mappings, collectionDTO.mappings);
		Assert.assertTrue(collectionDTO.size > 0);
		Assert.assertTrue(collectionDTO.documents == 0);

		Assert.assertTrue(dataStorageWithToken.collectionExists("test2"));
		Assert.assertTrue(dataStorageWithToken.collectionExists(collectionDTO));

		collections = dataStorageWithToken.getCollections();
		Assert.assertTrue(collections.size() == 2);

		// create one collection from DTO
		CollectionDTO newDTO = new CollectionDTO();
		newDTO.name = "test3";
		newDTO.mappings = mappings;

		collectionDTO = dataStorageWithToken.createCollection(newDTO);
		Assert.assertEquals("test3", collectionDTO.name);
		Assert.assertEquals(mappings, collectionDTO.mappings);
		Assert.assertTrue(collectionDTO.size > 0);
		Assert.assertTrue(collectionDTO.documents == 0);

		Assert.assertTrue(dataStorageWithToken.collectionExists("test3"));
		Assert.assertTrue(dataStorageWithToken.collectionExists(collectionDTO));

		collections = dataStorageWithToken.getCollections();
		Assert.assertTrue(collections.size() == 3);

		// update mappings of existing collection
		newDTO = dataStorageWithToken.getCollection("test1").get();
		newDTO.mappings = mappings;

		collectionDTO = dataStorageWithToken.updateMappings(newDTO);
		Assert.assertEquals("test1", collectionDTO.name);
		Assert.assertEquals(mappings, collectionDTO.mappings);
		Assert.assertTrue(collectionDTO.size > 0);
		Assert.assertTrue(collectionDTO.documents == 0);

		collections = dataStorageWithToken.getCollections();
		Assert.assertTrue(collections.size() == 3);

	}

}
