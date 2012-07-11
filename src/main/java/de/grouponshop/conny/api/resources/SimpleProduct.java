package de.grouponshop.conny.api.resources;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SimpleProduct {

	private String sku;
	private int lastImport;
	private int stock;
	
	private List<AttributeMapping> attributes;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@JsonProperty(value = "last_import")
	public int getLastImport() {
		return lastImport;
	}

	@JsonProperty(value = "last_import")
	public void setLastImport(int lastImport) {
		this.lastImport = lastImport;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public List<AttributeMapping> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeMapping> attributes) {
		this.attributes = attributes;
	}
}
