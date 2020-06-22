package xtensus.beans.common.GBO;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("request")
public class Group {
	private String uidd;
	private String groupName;
	private String description1;
	private String membre=" ";
	
	public Group(){
		this.uidd="";
		this.groupName="";
		this.description1="";
		this.membre="";
	}

	public String getUidd() {
		return uidd;
	}

	public void setUidd(String uidd) {
		this.uidd = uidd;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getMembre() {
		return membre;
	}

	public void setMembre(String membre) {
		this.membre = membre;
	}

	
}
