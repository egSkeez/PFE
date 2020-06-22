package xtensus.beans.common.GBO;
 

import javax.faces.context.FacesContext;

import java.io.Serializable;


public class UserBean implements Serializable {

	public String hidden1;
	public String hidden2;
	
	public String getHidden2() {
		return hidden2;
	}

	public void setHidden2(String hidden2) {
		this.hidden2 = hidden2;
	}

	public String getHidden1() {
		return hidden1;
	}

	public void setHidden1(String hidden1) {
		this.hidden1 = hidden1;
	}

	public String action(){
		
		String value = FacesContext.getCurrentInstance().
			getExternalContext().getRequestParameterMap().get("hidden1");
	    setHidden1(value);
    
	    return "start";
	}	
}