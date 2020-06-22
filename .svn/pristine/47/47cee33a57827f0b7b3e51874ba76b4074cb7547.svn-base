package xtensus.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class TransactionDocumentId implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3702724333466229493L;
	/**
	 * 
	 */
 
	private int idTransaction;
	private int idExpDest;
	private int idDocument;
	
	
	

	public TransactionDocumentId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionDocumentId(int idTransaction, int idExpDest,
			int idDocument) {
		super();
		this.idTransaction = idTransaction;
		this.idExpDest = idExpDest;
		this.idDocument = idDocument;
	}
	@Column(name = "idTransaction", nullable = false)
	public int getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(int idTransaction) {
		this.idTransaction = idTransaction;
	}
	@Column(name = "idExpDest", nullable = false)
	public int getIdExpDest() {
		return idExpDest;
	}
	public void setIdExpDest(int idExpDest) {
		this.idExpDest = idExpDest;
	}
	
	
	@Column(name = "idDocument", nullable = false)
	public int getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}
	
}
