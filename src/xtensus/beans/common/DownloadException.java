package xtensus.beans.common;

/**
 * Cette classe definit une exception lors du telechargement d'un docuement a partir du GED.
 * 
 * @author Fakhreddine DAKHLAOUI , Xtensus Tunisie
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DownloadException extends Exception {
		
	  public String toString() {
		  return "Ressource non trouvée" ;
	  }

}
