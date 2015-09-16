package com.carbone.storeId;

import java.util.Comparator;

import com.carbone.dataset.ChangeAgent;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.utils.CustomException;

public class StoreIdEditor extends ChangeAgent<StoreId,StoreIdLog>{

	@Override
	protected StoreId getRowFromLog(StoreIdLog rec) {
		StoreId newRec = new StoreId(rec.getOldName(),rec.getNewName());
		return newRec;
	}

	@Override
	protected StoreId getMatchFromLog(StoreIdLog rec) {
		StoreId match = new StoreId();
		match.setOldName(rec.getOldName());
		return match;
	}

	@Override
	protected LogAction getAction(StoreIdLog rec) {
		return rec.getAction();
	}

	@Override
	protected Comparator<StoreId> getComparitor(DataSet<StoreId> db) throws CustomException {
		return db.getComparator("OldName");
	}

}
