package xtensus.beans.utils;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.*;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import xtensus.beans.common.LanguageManagerBean;

public class ValidationMontant  implements Validator{
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	public ValidationMontant() {
	}
	@Override
	public void validate(FacesContext facesContext, UIComponent uIComponent,
			Object object) throws ValidatorException {
		
		String montant = (String) object;
		Double mnt=Double.parseDouble(montant);
		
		String erreurMontantZero=messageSource.getMessage("erreurMontantZero",
				new Object[] {}, lm.createLocal());
		if (mnt==0) {
			System.out.println("dant validateur"+erreurMontantZero);
			FacesMessage message = new FacesMessage();
			message.setSummary("* "+erreurMontantZero);
			
			throw new ValidatorException(message);
		}
		else 
			System.out.println("c'est une double :: "+mnt);
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

