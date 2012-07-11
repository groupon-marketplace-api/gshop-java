package de.grouponshop.conny.api;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.IOException;

import java.util.List;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import org.codehaus.jackson.annotate.*;

/**
 * Wraps error responses from the API.
 */
public class ApiErrorException extends IOException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7328529054997647276L;

	/**
     * The response object as returned by the API
     */
    @XmlRootElement
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transfer {
        
        public int code;
        public String message;
    }
    
    /**
     * Client response used to parse the error message etc.
     */
    ClientResponse response;
    
    /**
     * Parsed response body
     */
    List<Transfer> errors;
    
    /**
     * Construct from client response object
     */
    ApiErrorException(ClientResponse response) {
        
        this.response = response;
        
        parseResponse();
    }
    
    /**
     * Gets an error collection from the response body
     */
    protected void parseResponse() {
        
        GenericType<List<Transfer>> type = new GenericType<List<Transfer>>(){};
        List<Transfer> errors = response.getEntity(type);
        
        this.errors = errors;
    }
    
    /**
     * Collect messages from errors and return them, one line per message
     */
    @Override
    public String getMessage() {
        
        if (errors.isEmpty()) {
            return "Server did not provide any error details";
        }
        
        String message = "(" + getStatus() + ") ";
        
        for (Transfer err : errors) {
            message += err.message + "\n";
        }
        
        return message.trim();
    }
    
    /**
     * HTTP response code of the error
     */
    public int getStatus() {
        
        return response.getStatus();
    }
    
    /**
     * Raw response body
     */
    public String getResponseBody() {
        
        return response.getEntity(String.class);
    }
}