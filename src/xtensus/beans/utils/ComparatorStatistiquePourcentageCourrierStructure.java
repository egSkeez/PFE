package xtensus.beans.utils;

import java.util.Comparator;

public class ComparatorStatistiquePourcentageCourrierStructure  implements Comparator{
	@Override
	public int compare(Object o1, Object o2) {
	
		//sort by pourcentage
		if(o1 instanceof StatistiqueCourrierStructureByNature){
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature1=(StatistiqueCourrierStructureByNature) o1;
			StatistiqueCourrierStructureByNature statistiqueCourrierStructureNature2=(StatistiqueCourrierStructureByNature) o2; 
			return statistiqueCourrierStructureNature1.getPourcentage().compareTo(statistiqueCourrierStructureNature2.getPourcentage()) < 0 ? 1 :statistiqueCourrierStructureNature2.getPourcentage().compareTo(statistiqueCourrierStructureNature2.getPourcentage()) > 0 ? -1 : 0; 
		}
		return 0;
	}
}
