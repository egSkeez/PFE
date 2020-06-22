package xtensus.beans.utils;

import java.util.Comparator;

public class ComparatorStatistiqueCourrierOrganismeOrStructure implements Comparator{
    // Comparator used for 2 object 'StatistiqueCourrierStructure' and 'StatistiqueCourrierOrganisme'
	@Override
	public int compare(Object o1, Object o2) {
		if(o1 instanceof StatistiqueCourrierStructure){
			StatistiqueCourrierStructure statistiqueCourrierStructure1 = (StatistiqueCourrierStructure) o1;
			StatistiqueCourrierStructure statistiqueCourrierStructure2 = (StatistiqueCourrierStructure) o2;
			//trie : getPoucenage =>getIdUnit
			return statistiqueCourrierStructure1.getUnite().getIdUnit().compareTo(statistiqueCourrierStructure2.getUnite().getIdUnit()) < 0 ? 1 : statistiqueCourrierStructure1.getUnite().getIdUnit().compareTo(statistiqueCourrierStructure2.getUnite().getIdUnit()) > 0 ? -1 : 0; 
		}
		if(o1 instanceof StatistiqueCourrierOrganisme){
			StatistiqueCourrierOrganisme statistiqueCourrierOrganisme1 = (StatistiqueCourrierOrganisme) o1;
			StatistiqueCourrierOrganisme statistiqueCourrierOrganisme2 = (StatistiqueCourrierOrganisme) o2;
			return statistiqueCourrierOrganisme1.getPourcentage().compareTo(statistiqueCourrierOrganisme2.getPourcentage()) < 0 ? 1 : statistiqueCourrierOrganisme1.getPourcentage().compareTo(statistiqueCourrierOrganisme2.getPourcentage()) > 0 ? -1 : 0;
		}
		
		//sort by nombre de courriers
		if(o1 instanceof StatistiqueCourrierStructureByNature){
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature1=(StatistiqueCourrierStructureByNature) o1;
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature2=(StatistiqueCourrierStructureByNature) o2; 
			return statistiqueCourrierStructureNature1.getCountCourrier().compareTo(statistiqueCourrierStructureNature2.getCountCourrier()) < 0 ? 1 :statistiqueCourrierStructureNature2.getCountCourrier().compareTo(statistiqueCourrierStructureNature2.getCountCourrier()) > 0 ? -1 : 0; 
		}
		
		return 0;
	}

}
