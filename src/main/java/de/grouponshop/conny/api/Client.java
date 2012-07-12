package de.grouponshop.conny.api;

import javax.ws.rs.core.MultivaluedMap;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 * Simple entry point for communicating with the API, can be used
 * to create tokens and make unauthorized requests to the API.
 */
public class Client {
    
    /**
     * The main settings defining the behavior of the client:
     * - id: The client-id
     * - secret: The client-secret
     * - site: The URL of the API, including scheme and path
     */
    private String id, secret, site;
    
    /**
     * C-tor containing all the settings, just writin'
     */
    public Client(String clientId, String clientSecret, String site) {
        
        id = clientId;
        secret = clientSecret;
        this.site = site;
    }
    
    /**
     * Helper function to get the API-URL using the country code.
     * 
     * @param country ISO 3166 2-letter country code
     * 
     * @return Full API URL, e.g.: https://conny.grouponshop.de/api-v1
     */
    public static String getSiteFromCountry(String country) {
    	
    	if (country.equals("it")) {
    		return "https://conny.groupon-shop.it/api-v1";
    	}
        
        return "https://conny.grouponshop." + getTldFromCountry(country)
            + "/api-v1";
    }
    
    /**
     * Helper function to convert a country code to a top level domain
     * 
     * @param country ISO 3166 2-letter country code
     * 
     * @return Tld used for the given country
     */
    public static String getTldFromCountry(String country) {
        
        if (country.equals("gb") || country.equals("uk")) {
            return "co.uk";
        }
        
        if (country.equals("au")) {
            return "com." + country;
        }
        
        return country;
    }
    
    public String getResourcePackage() {
        
        return "de.grouponshop.conny.api.resources";
    }
    
    /**
     * Acquire an access token using the client credential-flow
     * 
     * For more information about the client credential-flow please
     * refer to section 4.4 of the oauth2 spec:
     * 
     * http://tools.ietf.org/html/draft-ietf-oauth-v2-27#section-4.4
     * 
     * @return A valid access token when successful, null otherwise
     */
    public AccessToken getClientCredentialToken()
        throws ApiErrorException {
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("grant_type", "client_credentials");
        params.add("client_id", id);
        params.add("client_secret", secret);
        
        return getToken(params);
    }
    
    /**
     * Let's pretend this would be a generic OAuth2-client
     */
    public String getTokenPath() {
        return "/token";
    }
    
    /**
     * Access to the internal jersey-client that is wrapped by this class
     */
    public com.sun.jersey.api.client.Client getJerseyClient() {
        com.sun.jersey.api.client.config.ClientConfig cc =
            new com.sun.jersey.api.client.config.DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        
        return com.sun.jersey.api.client.Client.create(cc);
    }
    
    /**
     * The token resource is the only one where we send our request
     * data urlencoded. So, specific accessor for that one.
     */
    protected WebResource.Builder getTokenResource() {
        WebResource.Builder resource = resource(getTokenPath())
                .type("application/x-www-form-urlencoded");
        return resource;
    }
    
    /**
     * Basic setup for getting a WebResource for a specifc path/resource. You'd
     * most likely only use AcccessToken.resource.
     */
    public WebResource.Builder resource(String path) {
        WebResource.Builder resource = getJerseyClient().resource(site + path)
            .type("application/json")
            .accept("application/json");
        
        return resource;
    }

	public WebResource.Builder resource(String path, MultivaluedMap<String, String> query) {
		
        WebResource.Builder resource = getJerseyClient().resource(site + path)
        		.queryParams(query)
                .type("application/json")
                .accept("application/json");
		
		return resource;
	}
    
    /**
     * General method for getting a token using any parameters you want
     */
    public AccessToken getToken(MultivaluedMap<String, String> bodyParams)
        throws ApiErrorException {
        
        ClientResponse response = getTokenResource()
            .post(ClientResponse.class, bodyParams);
        
        if (response.getStatus() >= 400) {
            throw new ApiErrorException(response);
            //return null;
        }
        
        AccessToken token =
            response.getEntity(AccessToken.class);
        token.setClient(this);
        
        return token;
    }
}
