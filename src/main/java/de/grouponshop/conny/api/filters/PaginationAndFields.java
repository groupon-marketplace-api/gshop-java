package de.grouponshop.conny.api.filters;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PaginationAndFields implements QueryFilter {
	
	int page;
	int pageSize;
	Set<String> fields;
	
	public PaginationAndFields() {
		
		fields = new HashSet<String>();
		page = 1;
		pageSize = 20;
	}
	
	public PaginationAndFields pageSize(int size) {
		pageSize = size;
		return this;
	}
	
	public PaginationAndFields page(int p) {
		page = p;
		return this;
	}
	
	public PaginationAndFields fields(String... fieldsArr) {
		for (String field : fieldsArr) {
			if (!fields.contains(field)) {
				fields.add(field);
			}
		}
		return this;
	}
	
	public String getFieldsString() {
		
		StringBuilder builder = new StringBuilder();
		
		for (String field : fields) {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(field);
		}
		
		return builder.toString();
	}
	
	public int getSkip() {
		return pageSize * (page - 1);
	}

	@Override
	public MultivaluedMap<String, String> toQueryParams() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		
		params.add("fields", getFieldsString());
		params.add("skip", Integer.toString(getSkip()));
		params.add("limit", Integer.toString(pageSize));
		
		return params;
	}

}
