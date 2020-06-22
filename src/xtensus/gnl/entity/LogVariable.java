package xtensus.gnl.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import xtensus.entity.Entite;

@Entity
@Table(name = "logvariable")
public class LogVariable extends Entite implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LogVariableId id;

	public LogVariable() {
	}

	public LogVariable(LogVariableId id) {
		setId(id);
	}
	
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "logId", column = @Column(name = "logId", nullable = false)),
			@AttributeOverride(name = "variableId", column = @Column(name = "variableId", nullable = false)) })
	public LogVariableId getId() {
		return id;
	}
	
	public void setId(LogVariableId id) {
		this.id = id;
	}
	

}
