package de.grouponshop.conny.api;

import javax.xml.bind.annotation.XmlRootElement;
import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.client.WebResource;
// import com.sun.jersey.api.client.ClientResponse;

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
public class AccessToken {
    
    /**
     * The client instance used to make requests to the API
     */
    Client client;
    
    /**
     * The actual token used for authorization
     */
    String token;
    
    /**
     * This class mirrors the structure of the API response for the /token
     * endpoint
     */
    @XmlRootElement
    public static class Transfer {
        
        /**
         * UNIX timestamp in seconds when this token expires
         */
        public int expires_at;
        
        /**
         * Time in seconds until the token expires, can be used to
         * check if server-time is as expected
         * 
         * @see expires_at
         */
        public int expires_in;
        
        /**
         * The client-id passed when creating this token
         */
        public String client_id;
        
        /**
         * Id of the merchant this token (and the client) belongs to
         */
        public int merchant_id;
        
        /**
         * The actual token
         */
        public String access_token;
        
        /**
         * Should always be "bearer"
         */
        public String token_type;
        
        /**
         * May contain specific permissions in the future, for now
         * it's always an empty string
         */
        public String scope;
    }
    
    /**
     * Create token directly from response data
     * 
     * @param client The Client instance used to make requests to the API
     * @param data Information about a previously acquired token
     */
    public AccessToken(Client client, Transfer data) {
        
        this.client = client;
        this.token = data.access_token;
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

        this.client = client;
        this.token = token;
    }
    
    /**
     * Prepare resource, uses client-settings and adds an Authorization
     * HTTP-header containing the token-string.
     * 
     * @param path Path of the resource, relative to /api-v1. E.g.: /product
     */
    public WebResource.Builder resource(String path) {
        
        return client.resource(path)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
    
    /**
     * Just output the raw token when converting this class to a string
     */
    @Override
    public String toString() {
        
        return token;
    }
}
