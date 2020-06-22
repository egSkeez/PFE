package xtensus.beans.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;

@Component()
@Scope("request")
public class ValidationVide implements Validator {
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LanguageManagerBean lm;

	private String message;
	private String message1;

	public ValidationVide() {
		lm = new LanguageManagerBean();

	}

	@Override
	public void validate(FacesContext facesContext, UIComponent uIComponent,
			Object object) throws ValidatorException {

		try {
		
			String uicom = uIComponent.getClientId(facesContext);
			int firstIndex = uicom.indexOf(":");
			int lastIndex = uicom.lastIndexOf(":");
			String deuxiemeString = uicom.substring(firstIndex + 1, lastIndex);
			int secondFirstIndex = deuxiemeString.indexOf(":");
			int componentndex = Integer.parseInt(deuxiemeString
					.substring(secondFirstIndex + 1));

			List<ComposantDynamique> listeComposants = vb
					.getListComposantDynamiqueTransmission();

			if (listeComposants != null) {
				ComposantDynamique composantDynamique = listeComposants
						.get(componentndex);
				
				if (composantDynamique != null) {
					if (composantDynamique.getPattern() != null
							&& composantDynamique.getPattern().trim().length() > 0) {
						System.out.println("dans if "+composantDynamique.getPattern().trim().length());
						if (composantDynamique.getPattern().equals("entier")) {
							boolean validObject = true;
							try {

								Integer.parseInt(object.toString());

							} catch (NumberFormatException e) {
								validObject = false;
							}
							System.out.println("###### validObject == "
									+ validObject);
							if (!validObject) {
								String messageAlert = composantDynamique
										.getMessageAlerte();
								System.out.println("##### messageAlert == "
										+ messageAlert);
								setMessage1(messageSource.getMessage(
										messageAlert, new Object[] {},
										lm.createLocal()));
								FacesContext.getCurrentInstance().addMessage(
										"messages",
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												message1, ""));
								// FacesMessage message = new FacesMessage();
								// message.setSummary("Nombre de pages Invalide");
								// throw new ValidatorException(message);
								vb.setNotAddCourrier(true);
								System.out.println("set 1");

							}else{
								vb.setNotAddCourrier(false);
							}
						} else {
							String messageAlert = composantDynamique
									.getMessageAlerte();
							System.out.println("##### messageAlert == "
									+ messageAlert);
							String pattern = composantDynamique.getPattern();
							System.out.println("##### pattern == " + pattern);
							String contenuchamp = (String) object;
							Pattern patternCin = Pattern.compile(pattern);
							Matcher matcherCin = patternCin
									.matcher(contenuchamp);
							boolean match = matcherCin.matches();
							System.out.println("###### match == " + match);
							if (!match) {
								setMessage(messageSource.getMessage(
										messageAlert, new Object[] {},
										lm.createLocal()));
								FacesContext.getCurrentInstance().addMessage(
										"messages",
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												message, ""));
								// FacesMessage message = new FacesMessage();
								// message.setSummary("Numéro CIN invalide");
								// throw new ValidatorException(message);
								vb.setNotAdd(true);
								
								System.out.println("set 2");

							}else{
								vb.setNotAdd(false);
							}
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message1) {
		this.message1 = message1;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public LanguageManagerBean getLm() {
		return lm;
	}

	public void setLm(LanguageManagerBean lm) {
		this.lm = lm;
	}

}
