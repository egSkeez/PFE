package xtensus.entity;

// Generated 6 déc. 2012 15:06:54 by Hibernate Tools 3.4.0.Beta1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Fax generated by hbm2java
 */
@Entity
@Table(name = "fax")
public class Fax extends xtensus.entity.Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6024002845884299786L;
	private Integer faxId;
	private String faxFilename;
	private String faxSenderNumber;
	private Date faxReceivedDate;
	private Integer faxNumberOfPages;
	private int faxIs_treated;

	public Fax() {
	}

	public Fax(String faxFilename, String faxSenderNumber,
			Date faxReceivedDate, Integer faxNumberOfPages, int faxIs_treated) {
		this.faxFilename = faxFilename;
		this.faxSenderNumber = faxSenderNumber;
		this.faxReceivedDate = faxReceivedDate;
		this.faxNumberOfPages = faxNumberOfPages;
		this.faxIs_treated = faxIs_treated;

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "faxId", unique = true, nullable = false)
	public Integer getFaxId() {
		return this.faxId;
	}

	public void setFaxId(Integer faxId) {
		this.faxId = faxId;
	}

	@Column(name = "faxFilename")
	public String getFaxFilename() {
		return this.faxFilename;
	}

	public void setFaxFilename(String faxFilename) {
		this.faxFilename = faxFilename;
	}

	@Column(name = "faxSenderNumber")
	public String getFaxSenderNumber() {
		return this.faxSenderNumber;
	}

	public void setFaxSenderNumber(String faxSenderNumber) {
		this.faxSenderNumber = faxSenderNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "faxReceivedDate", length = 19)
	public Date getFaxReceivedDate() {
		return this.faxReceivedDate;
	}

	public void setFaxReceivedDate(Date faxReceivedDate) {
		this.faxReceivedDate = faxReceivedDate;
	}

	@Column(name = "faxNumberOfPages")
	public Integer getFaxNumberOfPages() {
		return faxNumberOfPages;
	}

	public void setFaxNumberOfPages(Integer faxNumberOfPages) {
		this.faxNumberOfPages = faxNumberOfPages;
	}

	@Column(name = "is_treated", columnDefinition = "TINYINT")
	public int getFaxIs_treated() {
		return faxIs_treated;
	}

	public void setFaxIs_treated(int faxIs_treated) {
		this.faxIs_treated = faxIs_treated;
	}

}
