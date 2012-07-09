package de.grouponshop.conny.api;

import javax.xml.bind.annotation.XmlRootElement;
import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.client.WebResource;
// import com.sun.jersey.api.client.ClientResponse;

public class AccessToken {
    
    Client client;
    String token;
    
    @XmlRootElement
    public static class Transfer {
        
        public int expires_at;
        public int expires_in;
        
        public String client_id;
        public int merchant_id;
        
        public String access_token;
        public String token_type;
        public String scope;
    }
    
    public AccessToken(Client client, Transfer data) {
        
        this.client = client;
        this.token = data.access_token;
    }
    
    public WebResource.Builder resource(String path) {
        
        return client.resource(path)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
    
    @Override
    public String toString() {
        
        return token;
    }
}
