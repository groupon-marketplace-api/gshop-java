package de.grouponshop.conny.api.example;

public class DocExample {

    // class declaration of AccessToken:
    @org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessToken {
        public String access_token;
    }

    public static void main(String[] args) {
        // getting the token
        // 1. configure jersey to use the jackson json provider
        com.sun.jersey.api.client.config.ClientConfig cc =
                new com.sun.jersey.api.client.config.DefaultClientConfig();
        cc.getClasses().add(org.codehaus.jackson.jaxrs.JacksonJsonProvider.class);
        com.sun.jersey.api.client.Client client =
                com.sun.jersey.api.client.Client.create(cc);

        // 2. Build parameters to pass to the token creation
        javax.ws.rs.core.MultivaluedMap<String, String> params =
                new com.sun.jersey.core.util.MultivaluedMapImpl();
        params.add("grant_type", "client_credentials");
        params.add("client_id", "YOUR_CLIENT_ID");
        params.add("client_secret", "YOUR_CLIENT_SECRET");

        // 3. make the api-request
        com.sun.jersey.api.client.ClientResponse response = client
                .resource("http://conny.url/api-v1/token")
                .type("application/x-www-form-urlencoded")
                .accept("application/json")
                .post(com.sun.jersey.api.client.ClientResponse.class, params);

        if (response.getStatus() == 201) {
    
            // 4. creation of token successful: get the token-string
            AccessToken tokenObj = response.getEntity(AccessToken.class);
            String token = tokenObj.access_token;
    
            // 5. make an authenticated request
            response = client
                    .resource("http://conny.url/api-v1/product")
                    .accept("application/json")
                    .header(javax.ws.rs.core.HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .get(com.sun.jersey.api.client.ClientResponse.class);
    
            if (response.getStatus() == 200) {
                System.out.println(response.getEntity(String.class));
            } else {
                System.out.println("Get products failed with status " + response.getStatus());
            }
        } else {
            System.out.println("Authentication failed with status " + response.getStatus());
        }
    }
}