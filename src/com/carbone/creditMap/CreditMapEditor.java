package com.carbone.creditMap;

import java.util.Comparator;

import com.carbone.dataset.ChangeAgent;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.utils.CustomException;

public class CreditMapEditor extends ChangeAgent<CreditMap,CreditMapLog>{

	@Override
	protected CreditMap getRowFromLog(CreditMapLog rec) {
		CreditMap newRec = new CreditMap(rec.getTag(),rec.getMajor(),rec.getMinor());
		return newRec;
	}

	@Override
	protected CreditMap getMatchFromLog(CreditMapLog rec) {
		CreditMap match = new CreditMap();
		match.setTag(rec.getTag());
		match.setMajor(rec.getMajor());
		match.setMinor(rec.getMinor());
		return match;
	}

	@Override
	protected LogAction getAction(CreditMapLog rec) {
		return rec.getAction();
	}

	@Override
	protected Comparator<CreditMap> getComparitor(DataSet<CreditMap> db) throws CustomException {
		return db.getComparator("Tag");
	}
}
