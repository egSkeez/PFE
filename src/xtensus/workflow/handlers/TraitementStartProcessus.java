package xtensus.workflow.handlers;

public class TraitementStartProcessus {

	private String etatDebut;
	private long idNodeStart;
	private boolean resultat;
	private String commentaire;

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

	public void setEtatDebut(String etatDebut) {
		this.etatDebut = etatDebut;
	}

	public String getEtatDebut() {
		return etatDebut;
	}

	public void setIdNodeStart(long idNodeStart) {
		this.idNodeStart = idNodeStart;
	}

	public long getIdNodeStart() {
		return idNodeStart;
	}

}
