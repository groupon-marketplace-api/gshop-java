package de.grouponshop.conny.api;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import de.grouponshop.conny.api.annotate.RestResource;
import de.grouponshop.conny.api.filters.QueryFilter;
import de.grouponshop.conny.api.resources.Product;

/**
 * Entry class for making authorized requests
 * 
 * To get an instance of this class, either provide a valid token directly
 * or (more likely) let an instance of Client create one.
 * 
 * One you have an access token, you can use the resource-method to create
 * a WebResource which in turn can be queried for data. E.g.:
 * 
 * <pre>
 * AccessToken token = client.getClientCredentialToken();
 * System.out.println("Products: " + token.resource("/product").get(String.class));
 * </pre>
 * 
 * @todo Handle expired tokens here, maybe even a simple token storage solution
 *       so we don't reacquire tokens on program launch when the one from the
 *       last run would be perfectly fine still.
 */
@JsonIgnoreProperties({ "expires_at", "expires_in", "client_id", "merchant_id", "token_type", "scope" })
public class AccessToken {
    
    /**
     * The client instance used to make requests to the API
     */
    private Client client;
    
    /**
     * The actual token used for authorization
     */
    private String token;
    
    public AccessToken() {
    	
    	// create without any information
    }
    
    /**
     * Create token using the token string. May in the future
     * make an additional API request to fetch more information
     * about the token
     * 
     * @param client The Client instance used to make requests to the API
     * @param token The token-string
     */
    public AccessToken(Client client, String token) {

        this.setClient(client);
        this.setToken(token, true);
    }
    
    /**
     * Prepare resource, uses client-settings and adds an Authorization
     * HTTP-header containing the token-string.
     * 
     * @param path Path of the resource, relative to /api-v1. E.g.: /product
     */
    public WebResource.Builder resource(String path) {
        
        return authenticate(getClient().resource(path));
    }
    
    /**
     * Prepare resource including query string
     * 
     * @param path
     * @param query
     * @return
     */
    public WebResource.Builder resource(String path, MultivaluedMap<String, String> query) {
    	
    	return authenticate(getClient().resource(path, query));
    }
    
    public WebResource.Builder authenticate(WebResource.Builder request) {
    	
    	return request.header(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
    }
    
    /**
     * Get one item of a resource by its id
     * 
     * Will request the API and retrieve a single resource with default
     * filters. If you pass null as the id, it will treat the resource
     * itself as a "single item"-resource.
     * 
     * @param id
     * @param type
     * @return
     * @throws ApiErrorException
     */
    public <T> T getOne(String id, MultivaluedMap<String, String> queryParams, Class<T> type) throws ApiErrorException {
        
        RestResource desc = type.getAnnotation(RestResource.class);
        
        String path = id != null ? desc.path() + "/" + id : desc.path();
        System.out.println("Calling path: " + path);
        
        ClientResponse response = resource(path, queryParams).get(ClientResponse.class);
        
        if (response.getStatus() != 200) {
            
            throw new ApiErrorException(response);
        }
        
        return response.getEntity(type);
    }

    /**
     * Short-hand for getOne without query params
     * 
     * @param id
     * @param type
     * @return
     * @throws ApiErrorException
     */
	public <T> T getOne(String id, Class<T> type) throws ApiErrorException {
		
		return getOne(id, new MultivaluedMapImpl(), type);
	}
    
    /**
     * Query a resource collection
     * 
     * This will always try and convert the resource to a collection of T.
     * If you want a single item, use getOne(null, Product.class).
     * 
     * @param type
     * @return
     * @throws ApiErrorException
     */
    @SuppressWarnings("unchecked")
	public <T> List<T> get(Class<T> type, MultivaluedMap<String, String> queryParams) throws ApiErrorException {
    	
    	RestResource desc = type.getAnnotation(RestResource.class);
    	
    	ClientResponse response = resource(desc.path(), queryParams)
    			.get(ClientResponse.class);
    	
    	if (response.getStatus() != 200) {
    		
    		throw new ApiErrorException(response);
    	}
    	
    	// This code is a little bit ugly but that's Java's idea of
    	// generics, so... here we go:
    	if (type.equals(Product.class)) {
    		
    		List<Product> products = response.getEntity(new GenericType<List<Product>>(){});
    		return (List<T>) products;
    	} else {
    		throw new RuntimeException("Cannot parse type " + type);
    	}
    }
    
    /**
     * Short-hand for requesting a resource-collection with standard
     * filters
     * 
     * @param type
     * @return
     * @throws ApiErrorException
     */
    public <T> List<T> get(Class<T> type, QueryFilter filter) throws ApiErrorException {
    	
    	return get(type, filter.toQueryParams());
    }
    
    /**
     * Short-hand for requesting a resource-collection without query
     * parameters
     * 
     * @param type
     * @return
     * @throws ApiErrorException
     */
    public <T> List<T> get(Class<T> type) throws ApiErrorException {
    	
    	return get(type, new MultivaluedMapImpl());
    }
    
    /**
     * Just output the raw token when converting this class to a string
     */
    @Override
    public String toString() {
        
        return getToken();
    }
    
    public void setToken(String token, boolean loadInfo) {
    	
    	this.token = token;
    }

    @JsonProperty(value = "access_token")
	public String getToken() {
		return token;
	}

    @JsonProperty(value = "access_token")
	public void setToken(String token) {
    	
    	// set token but don't request info since this is
    	// used to serialize a token
    	setToken(token, false);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
