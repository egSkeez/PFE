package xtensus.beans.utils;

import java.util.Comparator;

import xtensus.entity.Mail;

public class ComparatorMail implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		Mail m1 = (Mail) o1;
		Mail m2 = (Mail) o2;
		return (m1.getMailReceivedDate().after(m2.getMailReceivedDate())) ? -1 : (m1.getMailReceivedDate().before(m2.getMailReceivedDate())) ? 1 : 0;
	}

}
