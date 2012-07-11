package de.grouponshop.conny.api.example;

import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import de.grouponshop.conny.api.Client;
import de.grouponshop.conny.api.AccessToken;
import de.grouponshop.conny.api.ApiErrorException;

import de.grouponshop.conny.api.resources.Image;
import de.grouponshop.conny.api.resources.Product;

// import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;

public class Application {
    
    public void run() {
        
        Properties props = new Properties();
        
        try {
            props.load(new FileInputStream("local.properties"));
        } catch (IOException e) {
            System.out.println(
                "Please provide a local.properties file. See example.properties for an example.");
            return;
        }
        
        String clientId = props.getProperty("oauth2.clientId");
        String clientSecret = props.getProperty("oauth2.clientSecret");
        String baseUrl = props.getProperty("conny.baseUrl");
        
        if (baseUrl == null) {
            baseUrl = Client.getSiteFromCountry(
                props.getProperty("conny.country")
            );
        }
        
        Client client = createClient(clientId, clientSecret, baseUrl);
        if (client == null) {
            System.out.println("Please provide oauth2.clientId and oauth2.clientSecret.");
            return;
        }
        
        try {
            
            AccessToken token = client.getClientCredentialToken();
            
            // test token with simple "GET /token"-call
            if (!checkToken(token)) {
                System.out.println("Simple GET /token failed. Token invalid or API broken: " + token);
                return;
            }
            System.out.println("Generated and tested token: " + token);
            
            Product p = token.getOne("1", Product.class);
            
            System.out.println("Loaded product: " + p.getSku() 
            		+ ", attribute set: " + p.getAttributeSet());
            System.out.println("The product has " + p.getImages().size() + " images.");
            
            for (Image img : p.getImages()) {
            	System.out.println(" - Image '" + img.getId() + "': " + img.getUrl());
            }
            
            List<Product> products = token.get(Product.class);
            
            System.out.println("Found " + products.size() + " products on first page.");
            for (Product product : products) {
            	
            	System.out.println(" - Product with sku " + product.getSku());
            }
        } catch (ApiErrorException e) {
            
            e.printStackTrace();
        }
    }
    
    protected boolean getProducts(AccessToken token) {
        
        ClientResponse response = token.resource("/product")
            .get(ClientResponse.class);
        
        if (response.getStatus() != 200) {
            System.out.println("Failed with " + response.getStatus()
                + ": " + response.getEntity(String.class));
            return false;
        }
        
        System.out.println("Products: " + response.getEntity(String.class));
        
        return true;
    }
    
    protected boolean checkToken(AccessToken token) {
        
        ClientResponse response = token.resource("/token")
            .get(ClientResponse.class);
        
        return response.getStatus() == 200;
    }
    
    protected Client createClient(String clientId, String clientSecret, String baseUrl) {
        
        if (clientId == null || clientSecret == null) {
            return null;
        }
        
        return new Client(clientId, clientSecret, baseUrl);
    }
    
    public static void main(String[] argv) {
        
        Application app = new Application();
        
        app.run();
    }
}
