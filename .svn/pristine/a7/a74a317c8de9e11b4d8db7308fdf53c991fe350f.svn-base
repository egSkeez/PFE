package xtensus.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactiondocument")
public class TransactionDocument  extends Entite{
private TransactionDocumentId id;
private Document document;
private Transaction transaction;
private Expdest expdest;


public TransactionDocument() {
	super();
	// TODO Auto-generated constructor stub
}

public TransactionDocument(TransactionDocumentId id) {
	super();
	this.id = id;
}

public TransactionDocument(TransactionDocumentId id, Document document,
		Transaction transaction, Expdest expdest) {
	super();
	this.id = id;
	this.document = document;
	this.transaction = transaction;
	this.expdest = expdest;
}

@EmbeddedId
@AttributeOverrides({
		@AttributeOverride(name = "idTransaction", column = @Column(name = "idTransaction", nullable = false)),
		@AttributeOverride(name = "idExpDest", column = @Column(name = "idExpDest", nullable = false)),
		@AttributeOverride(name = "idDocument", column = @Column(name = "idDocument", nullable = false)) })
public TransactionDocumentId getId() {
	return id;
}

public void setId(TransactionDocumentId id) {
	this.id = id;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="idDocument" ,nullable = false, insertable = false, updatable = false)
public Document getDocument() {
	return document;
}

public void setDocument(Document document) {
	this.document = document;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="idTransaction", nullable = false, insertable = false, updatable = false)
public Transaction getTransaction() {
	return transaction;
}

public void setTransaction(Transaction transaction) {
	this.transaction = transaction;
}
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="idExpDest",nullable = false, insertable = false, updatable = false)
public Expdest getExpdest() {
	return expdest;
}

public void setExpdest(Expdest expdest) {
	this.expdest = expdest;
}

}
