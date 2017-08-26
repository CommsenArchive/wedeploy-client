package com.commsen.wedeploy.client.data.test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.commsen.wedeploy.client.BaseTest;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.WeDeployDataBatchOperationResult;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;

public class WeDeployDocumentTest extends BaseTest {

	public WeDeployDocumentTest() throws WeDeployClientException, IOException {
		super();
	}

	SecureRandom random = new SecureRandom();

	@Test
	public void openCRUD() throws WeDeployClientException {

		dataStorageWithToken.getOrCreateCollection("open");
		WeDeployDataCollection collection = dataStorageNoToken.collection("open");

		commonCRUD(collection, collection, 100);
	}

	@Test
	public void writeProtectedCRUD() throws WeDeployClientException {

		dataStorageWithToken.getOrCreateCollection("writeProtected");
		WeDeployDataCollection readCollection = dataStorageNoToken.collection("writeProtected");
		WeDeployDataCollection writeCollection = dataStorageWithToken.collection("writeProtected");
		WeDeployDataCollection writeCollectionAuto = dataStorageWithAutoToken.collection("writeProtected");
			
		commonCRUD(readCollection, writeCollection, 100);
		commonCRUD(readCollection, writeCollectionAuto, 100);
	}


	@Test
	public void protectedCRUD() throws WeDeployClientException {

		dataStorageWithToken.getOrCreateCollection("protected");
		WeDeployDataCollection collection = dataStorageWithToken.collection("protected");
		commonCRUD(collection, collection, 100);
		collection = dataStorageWithAutoToken.collection("protected");
		commonCRUD(collection, collection, 100);
	}

	private void commonCRUD(WeDeployDataCollection readCollection, WeDeployDataCollection writeCollection, int bulkGenerate) throws WeDeployClientException {
		writeCollection.deleteAll();
		
		@SuppressWarnings("rawtypes")
		List<Map> result = readCollection.read();
		Assert.assertTrue(result.isEmpty());
		
		List<User> users = new ArrayList<>(bulkGenerate);
		for (int i = 0; i < bulkGenerate; i++) {
			users.add(randomUser());
		}
		
		WeDeployDataBatchOperationResult<User> batchSave = writeCollection.save(users);
		
		List<WeDeployDataDocument<User>> savedUsers = batchSave.getSuccesful();

		Assert.assertFalse(batchSave.hasFailures());
		Assert.assertTrue(batchSave.getFailed().isEmpty());
		Assert.assertEquals(users.size(), savedUsers.size());

		List<WeDeployDataDocument<User>> loadedUsers = readCollection.read(User.class);
	
		Assert.assertEquals(bulkGenerate, savedUsers.size());
		Assert.assertEquals(bulkGenerate, loadedUsers.size());

		for (int i = 0; i < bulkGenerate; i++) {
			Assert.assertEquals(users.get(i), savedUsers.get(i).getObject());
			Assert.assertEquals(users.get(i), loadedUsers.get(i).getObject());
		}
		
		loadedUsers = readCollection.read(User.class, 10, 0);
		Assert.assertEquals(10, loadedUsers.size());
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(users.get(i), loadedUsers.get(i).getObject());
		}
		
		loadedUsers = readCollection.read(User.class, 0, 10);
		Assert.assertEquals(90, loadedUsers.size());
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(users.get(i + 10), loadedUsers.get(i).getObject());
		}
		
		loadedUsers = readCollection.read(User.class, 10, 10);
		Assert.assertEquals(10, loadedUsers.size());
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(users.get(i + 10), loadedUsers.get(i).getObject());
		}
		
		User generatedUser = randomUser();
		WeDeployDataDocument<User> savedUser = writeCollection.save(generatedUser);
		result = readCollection.read();
		
		Assert.assertTrue(generatedUser.equals(savedUser.getObject()));
		Assert.assertNotNull(savedUser.getId());
		Assert.assertEquals(bulkGenerate + 1, result.size());

		generatedUser = randomUser();
		savedUser = writeCollection.update(savedUser.getId(), generatedUser);
		result = readCollection.read();

		Assert.assertTrue(generatedUser.equals(savedUser.getObject()));
		Assert.assertNotNull(savedUser.getId());
		Assert.assertEquals(bulkGenerate + 1, result.size());
	
		writeCollection.delete(savedUser.getId());
		result = readCollection.read();
		
		Assert.assertFalse(readCollection.exists(savedUser.getId()));
		Assert.assertEquals(bulkGenerate, result.size());
	}
	
	
	private User randomUser () {
		int nr = random.nextInt();
		User user = new User();
		user.name = "User " + nr;
		user.address.city = "City " + nr;
		user.address.street.name = nr + " Str ";
		user.address.street.number = nr;
		return user;
		
	}

	static class User {
		
		String name;
		
		Date lastLogin = new Date();
		
		Address address = new Address();

//		String id;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((address == null) ? 0 : address.hashCode());
			result = prime * result + ((lastLogin == null) ? 0 : lastLogin.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			User other = (User) obj;
			if (address == null) {
				if (other.address != null)
					return false;
			} else if (!address.equals(other.address))
				return false;
			if (lastLogin == null) {
				if (other.lastLogin != null)
					return false;
			} else if (!lastLogin.equals(other.lastLogin))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "User [name=" + name + ", lastLogin=" + lastLogin + ", address=" + address + "]";
		}
		
	}
	
	static class Address {
		String city;
		Street street = new Street();
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			result = prime * result + ((street == null) ? 0 : street.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Address other = (Address) obj;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			if (street == null) {
				if (other.street != null)
					return false;
			} else if (!street.equals(other.street))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Address [city=" + city + ", street=" + street + "]";
		}
	}

	static class Street {
		String name;
		int number;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + number;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Street other = (Street) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (number != other.number)
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Street [name=" + name + ", number=" + number + "]";
		}
	}}
