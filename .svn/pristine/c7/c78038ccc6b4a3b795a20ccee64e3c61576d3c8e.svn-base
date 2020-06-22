package xtensus.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RelancetransactionId implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3601319817027306235L;
	/**
	 * 
	 */
	private int relanceId;
	private int transactionId;

	public RelancetransactionId() {
	}

	public RelancetransactionId(int relanceId, int transactionId) {
		this.relanceId = relanceId;
		this.transactionId = transactionId;
	}

	@Column(name = "relanceId", nullable = false)
	public int getRelanceId() {
		return this.relanceId;
	}

	public void setRelanceId(int relanceId) {
		this.relanceId = relanceId;
	}

	@Column(name = "transactionId", nullable = false)
	public int getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RelancetransactionId))
			return false;
		RelancetransactionId castOther = (RelancetransactionId) other;

		return (this.getRelanceId() == castOther.getRelanceId())
				&& (this.getTransactionId() == castOther.getTransactionId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRelanceId();
		result = 37 * result + this.getTransactionId();
		return result;
	}

}
