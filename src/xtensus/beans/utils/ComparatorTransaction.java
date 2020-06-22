package xtensus.beans.utils;

import java.util.Comparator;

import xtensus.entity.Transaction;

public class ComparatorTransaction implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Transaction transaction1 = (Transaction) o1;
		Transaction transaction2 = (Transaction) o2;
		return transaction1.getTransactionId() < transaction2.getTransactionId() ? -1 : transaction1.getTransactionId() > transaction2.getTransactionId() ? 1 : 0;
	}

}
