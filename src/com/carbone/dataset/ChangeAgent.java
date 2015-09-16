package com.carbone.dataset;

import java.util.ArrayList;
import java.util.Comparator;

import com.carbone.utils.CustomException;
import com.carbone.utils.Log;


public abstract class ChangeAgent <Tdb,Tlog>{
	private final static String TAG = "ChangeAgent";

	protected abstract Tdb getRowFromLog(Tlog rec);
	protected abstract Tdb getMatchFromLog(Tlog rec);
	protected abstract LogAction getAction(Tlog rec);
	protected abstract Comparator<Tdb> getComparitor(DataSet<Tdb> db) throws CustomException;

	public  int applyLog(DataSet<Tdb> db, DataSet<Tlog> logFile) throws CustomException{

		int cnt = 0;
		for (Tlog rec : logFile){
			Tdb match = getMatchFromLog(rec);
			ArrayList<Integer> matches = db.contains(match);
			if (getAction(rec) == LogAction.DELETE){
				switch (matches.size()){
				case 0:
					Log.e(TAG, "No Record to Delete: " + rec.toString());
					throw new CustomException("ApplyLog Failed: No Record to Delete: " + rec.toString());
				case 1:
					db.deleteFirst(match);
					cnt++;
					break;
				default:
					Log.e(TAG, "Ambiguous Delete: " + rec.toString());
					throw new CustomException("ApplyLog Failed: Ambiguous Delete: " + rec.toString());
				}
			} else {
				//append
				if (matches.size() == 0){
					db.add(getRowFromLog(rec));
					cnt++;
				} else {
					Log.e(TAG, "Append Record Already Exists. No Action Taken: " + rec.toString());
					throw new CustomException("ApplyLog Failed: Append Record Already Exists. No Action Taken: " + rec.toString());
				}
			}
		}
		db.sort(getComparitor(db));
		db.save();
		return cnt;
	}
	
	public  int applyChanges(DataSet<Tdb> db, DataSet<Tlog> logFile, ArrayList<Tlog> changes) throws CustomException{
		int cnt = 0;
		for (Tlog rec : changes){
			Tdb match = getMatchFromLog(rec);
			ArrayList<Integer> matches = db.contains(match);
			if (getAction(rec) == LogAction.DELETE){
				switch (matches.size()){
				case 0:
					Log.e(TAG, "No Record to Delete: " + rec.toString());
					throw new CustomException("ApplyChanges Failed: No Record to Delete: " + rec.toString());
				case 1:
					db.deleteFirst(match);
					logFile.add(rec);
					break;
				default:
					Log.e(TAG, "Ambiguous Delete: " + rec.toString());
					throw new CustomException("ApplyChanges Failed: Ambiguous Delete: " + rec.toString());
				}
			} else {
				//append
				if (matches.size() == 0){
					db.add(getRowFromLog(rec));
					logFile.add(rec);
				} else {
					Log.e(TAG, "Append Record Already Exists. No Action Taken: " + rec.toString());
					throw new CustomException("ApplyChanges Failed: Append Record Already Exists. No Action Taken: " + rec.toString());
				}
			}
		}
		db.sort(getComparitor(db));
		db.save();
		logFile.save();
		return cnt;
	}
}
