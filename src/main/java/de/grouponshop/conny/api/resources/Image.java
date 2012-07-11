package de.grouponshop.conny.api.resources;

import org.codehaus.jackson.annotate.JsonProperty;

public class Image {

	private String id;
	private String sourceUrl;
	private String url;
	private boolean isMain;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty(value = "source_url")
	public String getSourceUrl() {
		return sourceUrl;
	}
	@JsonProperty(value = "source_url")
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@JsonProperty(value = "is_main")
	public boolean isMain() {
		return isMain;
	}
	@JsonProperty(value = "is_main")
	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}
}
