package xtensus.beans.common.GBO;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;




import xtensus.entity.UAgentInfo;

import xtensus.services.ApplicationManager;

@Component
@Scope("request") 
public class UserAgentProcessor {

	private static final long serialVersionUID = 1L;  
	//[XTE] : Classe enCapsule toute la logique de détection des périphériques mobile
    private UAgentInfo uAgentInfo; 
    private int x=1;
	public UserAgentProcessor() {

	}

	@Autowired
	public UserAgentProcessor(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		
	}
	
	  
    @PostConstruct  
    public void init() {  
    	System.out.println("Initialise ");
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();  
        String userAgentStr = request.getHeader("user-agent");  
        System.out.println("userAgentStr :"+userAgentStr);
        String httpAccept = request.getHeader("Accept");  
        System.out.println("httpAccept :"+httpAccept);

        uAgentInfo = new UAgentInfo(userAgentStr, httpAccept);  
    }

	public void setuAgentInfo(UAgentInfo uAgentInfo) {
		this.uAgentInfo = uAgentInfo;
	}

	public UAgentInfo getuAgentInfo() {
		return uAgentInfo;
	}

	  public boolean isPhone() {  
	        //Detects a whole tier of phones that support similar functionality as the iphone  
	        return uAgentInfo.detectTierIphone();  
	    }  
	  
	    public boolean isTablet() {  
	        // Will detect iPads, Xooms, Blackberry tablets, but not Galaxy - they use a strange user-agent  
	        return uAgentInfo.detectTierTablet();  
	    }  
	  
	    public boolean isMobile() {  
	        return isPhone() || isTablet();  
	    }

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		} 

	

}
