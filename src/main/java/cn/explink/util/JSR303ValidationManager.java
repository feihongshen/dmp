package cn.explink.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.apache.log4j.Logger;

import cn.explink.util.AjaxResult;

/**
 * JSR303验证工具类
 * <p/>
 * 基于 hibernate实现
 * @author timo.li
 * @since 1.0
 */
public class JSR303ValidationManager {

	private static JSR303ValidationManager jSR303ValidationManager;

	private String providerClassName = "org.hibernate.validator.HibernateValidator";

	private ValidatorFactory validationFactory;

	protected Class<? extends ValidationProvider> providerClass;

	private final Logger log = Logger.getLogger(this.getClass());

	private JSR303ValidationManager() {

		log.info("Initializing bean validation factory to get a validator");
		try {
			this.providerClass = (Class<? extends ValidationProvider>) Class
					.forName(providerClassName);
			log.info(this.providerClass.getName() + " validator found");
		} catch (ClassNotFoundException e) {
			log.error("Unable to find any bean validator implimentation for "
					+ providerClassName);
			log.error("Unable to load bean validation prvider class", e);
		}
		Configuration configuration = (this.providerClass != null ? Validation
				.byProvider(this.providerClass).configure() : Validation
				.byDefaultProvider().configure());
		if (configuration != null) {

			this.validationFactory = configuration.buildValidatorFactory();

		}

	}

	public static JSR303ValidationManager getInstance() {
		if (jSR303ValidationManager == null) {
			synchronized (JSR303ValidationManager.class) {
				if (jSR303ValidationManager == null) {
					jSR303ValidationManager = new JSR303ValidationManager();
				}
			}
		}
		return jSR303ValidationManager;
	}

	public Validator getValidator() {
		return this.validationFactory.getValidator();
	}
	
	public <T> AjaxResult doValidateAsAjaxResult(T validationObj ){
		try{
			Set<ConstraintViolation<T>> constraintViolations =this.validationFactory.getValidator().validate(validationObj);
			return ConstraintViolationToAjaxResult(constraintViolations);
		}catch(Exception ex){
		    return new AjaxResult(false, ex.getMessage());
		}
	}
	
	public <T> AjaxResult doValidateAsAjaxResult(T validationObj,Class<?>... groups ){
		try{
			Set<ConstraintViolation<T>> constraintViolations =this.validationFactory.getValidator().validate(validationObj,groups);
			return ConstraintViolationToAjaxResult(constraintViolations);
		}catch(Exception ex){
		    return new AjaxResult(false, ex.getMessage());
		}
	}
	
	public <T> Set<ConstraintViolation<T>>doValidate(T validationObj ){
		return this.validationFactory.getValidator().validate(validationObj);
	}
	
	public <T> AjaxResult ConstraintViolationToAjaxResult(Set<ConstraintViolation<T>> constraintViolations){
		AjaxResult ret=new AjaxResult(true, "");
		if(constraintViolations!=null&&constraintViolations.size()>0){
			ret.setResult(false);
			ret.setFieldErrors(new HashMap<String, List<String>>());
			boolean firstError=true;
			for(ConstraintViolation<T> constraintViolation:constraintViolations){
				String fiedName=constraintViolation.getPropertyPath().toString();
				if(!ret.getFieldErrors().containsKey(fiedName)){
					ret.getFieldErrors().put(fiedName, new ArrayList<String>());
				}
				if(firstError){
				    ret.setField(fiedName);
					ret.setMessage(constraintViolation.getMessage());
					firstError=false;
				}
				List<String> errors= (List<String>) ret.getFieldErrors().get(fiedName);
				if(errors!=null)errors.add(constraintViolation.getMessage());
			}
		}
		return ret;
	}

}
