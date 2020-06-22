package xtensus.beans.common.GBO;

import java.math.BigInteger;
import java.util.List;

import xtensus.entity.Courrier;
import xtensus.entity.CourrierDossier;
import xtensus.entity.Entite;
import xtensus.entity.Expdest;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionDestination;
import xtensus.entity.Transmission;

public class ModelCourrierComplet {
	

	
private Courrier courrier;
private Transmission transmission;
private CourrierDossier courrierDossier;
private Transaction transaction ;
private Expdest expdest;
private List<TransactionDestination> listTransactionDestination;
public Courrier getCourrier() {
	return courrier;
}
public void setCourrier(Courrier courrier) {
	this.courrier = courrier;
}
public Transmission getTransmission() {
	return transmission;
}
public void setTransmission(Transmission transmission) {
	this.transmission = transmission;
}
public CourrierDossier getCourrierDossier() {
	return courrierDossier;
}
public void setCourrierDossier(CourrierDossier courrierDossier) {
	this.courrierDossier = courrierDossier;
}
public Transaction getTransaction() {
	return transaction;
}
public void setTransaction(Transaction transaction) {
	this.transaction = transaction;
}
public Expdest getExpdest() {
	return expdest;
}
public void setExpdest(Expdest expdest) {
	this.expdest = expdest;
}
public List<TransactionDestination> getListTransactionDestination() {
	return listTransactionDestination;
}
public void setListTransactionDestination(List<TransactionDestination> listTransactionDestination) {
	this.listTransactionDestination = listTransactionDestination;
}




	

}
