package com.tech.webservice;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tech.service.interfaces.GenericService;
import com.tech.webservice.response.error.ValidationError;


public abstract class AbstractRestService<T> {

	
	@Autowired
	private MessageSource messageSource;
	
	private final GenericService<T,Long> bussinessService;
	
	
	protected AbstractRestService(final GenericService<T,Long> bussinessService){
		this.bussinessService = bussinessService;
	}
	
	
	@RequestMapping( method = RequestMethod.GET,headers="Accept=application/json")
	public List<T> getAll() {
		return getService().findAll();
	}
	
	
	// test will it work for single as well as for bulk update 
	@RequestMapping(value="updateAll", method = RequestMethod.PUT,headers="Accept=application/json")
	public String updateAll(@RequestBody List<T> entities) {
		for (T entity : entities) {
			getService().update(entity);
		}
		return "update success message ";
	}
	
	
	@RequestMapping( method = RequestMethod.DELETE,headers="Accept=application/json")
	public String deleteAll() {
		//return getService().delete(entity);
		return "deleted all element success message";
	}
	
	
	
	// operations for single object
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,headers="Accept=application/json")
	public T get( @PathVariable Long id) {
		return (T) getService().getById(id);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE,headers="Accept=application/json")
	public T delete(@PathVariable Long id) {
		//return getService().delete(id);;
		 T entity = (T) getService().getById(id);
		 getService().delete( entity );
		 return entity;
	}
	
	
	@RequestMapping( method = RequestMethod.POST,headers="Accept=application/json")
	public T create( @RequestBody @Valid T entity) {
		System.out.println("save this object \n "+entity);
		return (T) getService().persist(entity);
	}
	
	@RequestMapping( method = RequestMethod.PUT,headers="Accept=application/json")
	public T update(@RequestBody @Valid T entity) {
			return getService().update(entity);
	}
	
	
	// validation error handling 
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
 
        return processFieldErrors(fieldErrors);
    }
	
	
	
	
	
	@RequestMapping( value="/query", method = RequestMethod.POST,headers="Accept=application/json")
	public List<T> queryByExample(@RequestBody final T exampleInstance) {
		return  getService().findByExample(exampleInstance);
	}
	
	@RequestMapping( value="/exist", method = RequestMethod.POST,headers="Accept=application/json")
	public boolean isRecoredExist(@RequestBody final T exampleInstance) {
		System.out.println(" calling service to test recored exist or not ???? ");
		return  getService().findByExample(exampleInstance) == null ? true: false;
	}
	
	
	@RequestMapping( value="/exist/name", method = RequestMethod.GET)
	public boolean isNameExist(@RequestParam(value="name", required=true) final String name) {
		System.out.println(" calling service to test name exist or not in the database \n user input value for name is - "+name);
		return  getService().findByName(name) == null ? true: false;
	}
	
	
	protected GenericService<T,Long> getService(){
		return this.bussinessService;
	}
	
	
	protected ValidationError processFieldErrors(List<FieldError> fieldErrors) {
		ValidationError validationErrorWrapper = new ValidationError();
 
        for (FieldError fieldError: fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            validationErrorWrapper.addFieldError(fieldError.getField(), localizedErrorMessage);
        }
 
        return validationErrorWrapper;
    }
 
    protected String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale =  LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
 
        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }
 
        return localizedErrorMessage;
    }
	
}
