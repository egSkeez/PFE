package xtensus.beans.utils;

import java.util.Comparator;

import xtensus.entity.FaxMail;

public class ComparatorFaxMail implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		FaxMail m1 = (FaxMail) o1;
		FaxMail m2 = (FaxMail) o2;
		return (m1.getFaxMailReceivedDate().after(m2.getFaxMailReceivedDate())) ? -1 : (m1.getFaxMailReceivedDate().before(m2.getFaxMailReceivedDate())) ? 1 : 0;
	}

}
