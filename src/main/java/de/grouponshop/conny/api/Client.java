package de.grouponshop.conny.api;

import javax.ws.rs.core.MultivaluedMap;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

public class Client {
    
    private String id, secret, site;
    
    public Client(String clientId, String clientSecret, String site) {
        
        id = clientId;
        secret = clientSecret;
        this.site = site;
    }
    
    public static String getSiteFromCountry(String country) {
        
        return "https://conny.grouponshop." + getTldFromCountry(country)
            + "/api-v1";
    }
    
    public static String getTldFromCountry(String country) {
        return country;
    }
    
    public AccessToken getClientCredentialToken() {
        MultivaluedMap params = new MultivaluedMapImpl();
        params.add("grant_type", "client_credentials");
        params.add("client_id", id);
        params.add("client_secret", secret);
        
        return getToken(params);
    }
    
    public String getTokenPath() {
        return "/token";
    }
    
    public com.sun.jersey.api.client.Client getJerseyClient() {
        com.sun.jersey.api.client.config.ClientConfig cc =
            new com.sun.jersey.api.client.config.DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        
        return com.sun.jersey.api.client.Client.create(cc);
    }
    
    protected WebResource.Builder getTokenResource() {
        WebResource.Builder resource = resource(getTokenPath())
                .type("application/x-www-form-urlencoded");
        return resource;
    }
    
    public WebResource.Builder resource(String path) {
        WebResource.Builder resource = getJerseyClient().resource(site + path)
            .type("application/json")
            .accept("application/json");
        
        return resource;
    }
    
    public AccessToken getToken(MultivaluedMap bodyParams) {
        
        ClientResponse response = getTokenResource()
            .post(ClientResponse.class, bodyParams);
        
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed (" + site + ", " + response.getStatus() + "): "
                + response.getEntity(String.class));
        }
        
        AccessToken.Transfer transfer =
            response.getEntity(AccessToken.Transfer.class);
        
        return new AccessToken(this, transfer);
    }
}
