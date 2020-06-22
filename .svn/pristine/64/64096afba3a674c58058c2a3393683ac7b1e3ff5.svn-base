package xtensus.beans.utils;

import javax.faces.validator.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import xtensus.beans.common.LanguageManagerBean;

import java.util.regex.*;
public class ValidationTelephone  implements Validator {
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	public ValidationTelephone() {
	}

	public void validate(FacesContext facesContext, UIComponent uIComponent,
			Object object) throws ValidatorException {
		String enteredTel = (String) object;
		Pattern p = Pattern.compile("[\\s\\+0-9()]*[\\s0-9-]+");
		Matcher m = p.matcher(enteredTel);
		boolean matchFound = m.matches();

		if (!matchFound) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Téléphone invalide");
			
			throw new ValidatorException(message);
		}
	}

	public void setLm(LanguageManagerBean lm) {
		this.lm = lm;
	}

	public LanguageManagerBean getLm() {
		return lm;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

