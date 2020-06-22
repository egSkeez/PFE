package xtensus.beans.utils;

import javax.faces.validator.*;
import javax.faces.application.*;
import javax.faces.component.*;
import javax.faces.context.*;
import java.util.regex.*;

public class Validation implements Validator {
	public Validation() {
	}

	public void validate(FacesContext facesContext, UIComponent uIComponent,
			Object object) throws ValidatorException {
		String enteredEmail = (String) object;
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(enteredEmail);
		boolean matchFound = m.matches();

		if (!matchFound) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Invalid Email ID.");
			throw new ValidatorException(message);
		}
	}
}