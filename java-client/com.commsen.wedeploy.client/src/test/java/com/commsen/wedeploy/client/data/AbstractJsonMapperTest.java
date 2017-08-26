package com.commsen.wedeploy.client.data;

import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;


public abstract class AbstractJsonMapperTest {

	protected WeDeployDataMapper jsonMapper;

	@Test
	public void test() {
		
		Item item = new Item();
		String json = jsonMapper.toJson(item);
		Assert.assertFalse(json.contains("\"id\":\"" + item.name + "\""));
		Item result = jsonMapper.fromJson(json, Item.class);
		Assert.assertEquals(item, result);
		
		WeDeployDataDocument<Item> itemDoc = new WeDeployDataDocument<Item>(item.name, item);
		json = jsonMapper.toJson(itemDoc);
		Assert.assertTrue(json.contains("\"id\":\"" + item.name + "\""));
		result = jsonMapper.fromJson(json, Item.class);
		Assert.assertEquals(item, result);
		
		itemDoc = new WeDeployDataDocument<Item>(null, item);
		json = jsonMapper.toJson(itemDoc);
		Assert.assertFalse(json.contains("\"id\":\"" + item.name + "\""));
		result = jsonMapper.fromJson(json, Item.class);
		Assert.assertEquals(item, result);

		itemDoc = new WeDeployDataDocument<Item>("", item);
		json = jsonMapper.toJson(itemDoc);
		Assert.assertFalse(json.contains("\"id\":\"" + item.name + "\""));
		result = jsonMapper.fromJson(json, Item.class);
		Assert.assertEquals(item, result);

		ItemWithId itemWithId = new ItemWithId();
		json = jsonMapper.toJson(itemWithId);
		Assert.assertFalse(json.contains("\"id\":\"" + itemWithId.name + "\""));
		Assert.assertTrue(json.contains("\"id\":\"" + itemWithId.id + "\""));
		ItemWithId resultWithId = jsonMapper.fromJson(json, ItemWithId.class);
		Assert.assertEquals(itemWithId, resultWithId);
		
		item = new ItemWithId();
		json = jsonMapper.toJson(item);
		Assert.assertFalse(json.contains("\"id\":\"" + item.name + "\""));
		Assert.assertTrue(json.contains("\"id\":\"" + ((ItemWithId)item).id + "\""));
		resultWithId = jsonMapper.fromJson(json, ItemWithId.class);
		Assert.assertEquals(item, resultWithId);
		Assert.assertEquals((ItemWithId)item, resultWithId);
		
	}

	static class ItemWithId extends Item {
		String id =  UUID.randomUUID().toString();
	}
	
	static class Item {
		String name = UUID.randomUUID().toString();
		Date date = new Date();
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((date == null) ? 0 : date.hashCode());
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
			Item other = (Item) obj;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
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
			return "Item [name=" + name + ", date=" + date + "]";
		}
	
	}
}
