package de.grouponshop.conny.api.resources;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import de.grouponshop.conny.api.annotate.RestResource;

@RestResource(
    path="/product",
    filters="de.grouponshop.conny.api.filters.SkipLimitFields"
)
public class Product {
	
    private String sku;
    private String attributeSet;
    
    private List<AttributeMapping> attributes;
    private List<SimpleProduct> simples;
    private List<Image> images;
    
    private int lastImport;
    
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	@JsonProperty(value = "attribute_set")
	public String getAttributeSet() {
		return attributeSet;
	}
	
	@JsonProperty(value = "attribute_set")
	public void setAttributeSet(String attributeSet) {
		this.attributeSet = attributeSet;
	}

	public List<AttributeMapping> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeMapping> attributes) {
		this.attributes = attributes;
	}

	public List<SimpleProduct> getSimples() {
		return simples;
	}

	public void setSimples(List<SimpleProduct> simples) {
		this.simples = simples;
	}

	@JsonProperty(value = "last_import")
	public int getLastImport() {
		return lastImport;
	}

	@JsonProperty(value = "last_import")
	public void setLastImport(int lastImport) {
		this.lastImport = lastImport;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
