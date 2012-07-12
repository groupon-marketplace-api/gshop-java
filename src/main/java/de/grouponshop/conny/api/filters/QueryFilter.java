package de.grouponshop.conny.api.filters;

import javax.ws.rs.core.MultivaluedMap;

public interface QueryFilter {

	MultivaluedMap<String, String> toQueryParams();
}
