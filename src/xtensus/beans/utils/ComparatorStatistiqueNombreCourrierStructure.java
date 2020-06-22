package xtensus.beans.utils;

import java.util.Comparator;

public class ComparatorStatistiqueNombreCourrierStructure  implements Comparator{
	@Override
	public int compare(Object o1, Object o2) {
		
		//sor by nombre de courriers
		if(o1 instanceof StatistiqueCourrierStructureByNature){
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature1=(StatistiqueCourrierStructureByNature) o1;
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature2=(StatistiqueCourrierStructureByNature) o2; 
			return statistiqueCourrierStructureNature1.getCountCourrier().compareTo(statistiqueCourrierStructureNature2.getCountCourrier()) < 0 ? 1 :statistiqueCourrierStructureNature2.getCountCourrier().compareTo(statistiqueCourrierStructureNature2.getCountCourrier()) > 0 ? -1 : 0; 
		}
		if(o1 instanceof StatistiqueCourrierTempsReponseParUniteNature){
			StatistiqueCourrierTempsReponseParUniteNature statistiqueParTempsDeReponse1=(StatistiqueCourrierTempsReponseParUniteNature) o1;
			StatistiqueCourrierTempsReponseParUniteNature statistiqueParTempsDeReponse2=(StatistiqueCourrierTempsReponseParUniteNature) o2;
			return statistiqueParTempsDeReponse1.getNbreCourrierTraiteHorsDelai().compareTo(statistiqueParTempsDeReponse2.getNbreCourrierTraiteHorsDelai()) < 0 ? 1 :statistiqueParTempsDeReponse2.getNbreCourrierTraiteHorsDelai().compareTo(statistiqueParTempsDeReponse2.getNbreCourrierTraiteHorsDelai()) > 0 ? -1 : 0; 

		}
		return 0;
	}
}
