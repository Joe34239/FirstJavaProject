package com.carbone.category;

import java.util.Comparator;

import com.carbone.dataset.ChangeAgent;
import com.carbone.dataset.DataSet;
import com.carbone.dataset.LogAction;
import com.carbone.utils.CustomException;

public class CategoryEditor extends ChangeAgent<Category,CategoryLog>{

	@Override
	protected Category getRowFromLog(CategoryLog rec) {
		Category newRec = new Category(rec.getName());
		return newRec;
	}

	@Override
	protected Category getMatchFromLog(CategoryLog rec) {
		Category match = new Category();
		match.setName(rec.getName());
		return match;
	}

	@Override
	protected LogAction getAction(CategoryLog rec) {
		return rec.getAction();
	}

	@Override
	protected Comparator<Category> getComparitor(DataSet<Category> db) throws CustomException {
		return db.getComparator("Category");
	}

}
