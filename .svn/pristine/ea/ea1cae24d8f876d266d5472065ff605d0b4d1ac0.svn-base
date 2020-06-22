package xtensus.beans.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class GMAILAuthenticator extends Authenticator {
      
	private String user;
	private String pwd;
	
	public GMAILAuthenticator(String userName,String passWord){
		super();
		this.user = userName;
		this.pwd = passWord;
	}
	
	public PasswordAuthentication getPasswordAuthentication()
    {
       return new PasswordAuthentication(user, pwd);
    }
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
	
	
	
	
	
	
}
