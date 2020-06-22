package xtensus.gnl.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import xtensus.entity.Entite;

@Entity
@Table(name = "notificationvariable")
public class NotificationVariable extends Entite implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NotificationVariableId id;

	public NotificationVariable() {
	}

	public NotificationVariable(NotificationVariableId id) {
		setId(id);
	}
	
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "notificationId", column = @Column(name = "notificationId", nullable = false)),
			@AttributeOverride(name = "variableId", column = @Column(name = "variableId", nullable = false)) })
	public NotificationVariableId getId() {
		return id;
	}
	
	public void setId(NotificationVariableId id) {
		this.id = id;
	}
	

}
