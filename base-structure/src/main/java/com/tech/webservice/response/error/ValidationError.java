package com.tech.webservice.response.error;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {
	private List<Error> fieldErrors = new ArrayList<Error>();
	 
    public ValidationError() {
 
    }
 
    public void addFieldError(String path, String message) {
        fieldErrors.add(new Error(path, message));
    }

	public List<Error> getFieldErrors() {
		return fieldErrors;
	}

	@Override
	public String toString() {
		return "ValidationError [fieldErrors=" + fieldErrors + "]";
	}
    
}