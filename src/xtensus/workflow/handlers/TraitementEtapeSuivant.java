package xtensus.workflow.handlers;

public class TraitementEtapeSuivant {
	private long idNodeSuivante;
	private String etatActuelle;
	private String etatSuivant;
	private String etatPrecedente;
	private boolean resultat;
	private String commentaire;

	public String getEtatActuelle() {
		return etatActuelle;
	}

	public void setEtatActuelle(String etatActuelle) {
		this.etatActuelle = etatActuelle;
	}

	public String getEtatSuivant() {
		return etatSuivant;
	}

	public void setEtatSuivant(String etatSuivant) {
		this.etatSuivant = etatSuivant;
	}

	public boolean isResultat() {
		return resultat;
	}

	public void setResultat(boolean resultat) {
		this.resultat = resultat;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setEtatPrecedente(String etatPrecedente) {
		this.etatPrecedente = etatPrecedente;
	}

	public String getEtatPrecedente() {
		return etatPrecedente;
	}

	public void setIdNodeSuivante(long idNodeSuivante) {
		this.idNodeSuivante = idNodeSuivante;
	}

	public long getIdNodeSuivante() {
		return idNodeSuivante;
	}

}
