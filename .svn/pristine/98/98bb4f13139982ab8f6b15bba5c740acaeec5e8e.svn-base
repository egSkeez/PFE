package xtensus.beans.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidationCIN implements Validator {

	public ValidationCIN(){
		
	}
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uIComponent,
			Object object)
			throws ValidatorException {
		String cin = (String) object;
		Pattern patternCin = Pattern.compile("[0-9]*");
		Matcher matcherCin = patternCin.matcher(cin);
		boolean match = matcherCin.matches();
		if(!match){
			FacesMessage message = new FacesMessage();
			message.setSummary("CIN invalide");
			throw new ValidatorException(message);
		}
		
	}

}
