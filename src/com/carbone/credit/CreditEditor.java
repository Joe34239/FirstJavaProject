package com.carbone.credit;

import java.util.Comparator;

import com.carbone.dataset.ChangeAgent;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.utils.CustomException;

public class CreditEditor extends ChangeAgent<Credit,CreditLog>{

	@Override
	protected Credit getRowFromLog(CreditLog rec) {
		Credit newRec = Credit.createCredit(rec.getStore(), 
				rec.getDate(), 
				rec.getAmount(), 
				rec.getLocation(), 
				rec.getMajor(), 
				rec.getMinor(), 
				rec.isIgnore());
		return newRec;
	}

	@Override
	protected Credit getMatchFromLog(CreditLog rec) {
		Credit match = new Credit();
		match.setStore(rec.getStore());
		match.setDate(rec.getDate());
		match.setAmount(rec.getAmount());
		return match;
	}

	@Override
	protected LogAction getAction(CreditLog rec) {
		return rec.getAction();
	}

	@Override
	protected Comparator<Credit> getComparitor(DataSet<Credit> db) throws CustomException {
		return db.getComparator("Store");
	}
}
