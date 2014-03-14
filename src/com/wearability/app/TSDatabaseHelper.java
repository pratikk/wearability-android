package com.wearability.app;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TSDatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";

	private static final int DATABASE_VERSION = 1;
    
	private static final String DATABASE_NAME = "EMGDataManager";
	
    private static final String TABLE_LEFT_BICEP = "leftBicep";
    private static final String TABLE_RIGHT_BICEP = "rightBicep";
    private static final String TABLE_LEFT_TRICEP = "leftTricep";
    private static final String TABLE_RIGHT_TRICEP = "rightTricep";
    
    private static final String DATABASE_LEFT_BICEP_CREATE =
                "CREATE TABLE " + TABLE_LEFT_BICEP + " (" +
                "TIME" + " INTEGER, " +
                "GROUP" + "STRING," +		
                "VALUE"+ " INTEGER);";
    
    private static final String DATABASE_RIGHT_BICEP_CREATE =
            "CREATE TABLE " + TABLE_RIGHT_BICEP + " (" +
            "TIME" + " INTEGER, " +
            "GROUP" + "STRING," +		
            "VALUE"+ " INTEGER);";
    
    
    private static final String DATABASE_LEFT_TRICEP_CREATE =
            "CREATE TABLE " + TABLE_LEFT_TRICEP + " (" +
            "TIME" + " INTEGER, " +
            "GROUP" + "STRING," +		
            "VALUE"+ " INTEGER);";
    
    
    private static final String DATABASE_RIGHT_TRICEP_CREATE =
            "CREATE TABLE " + TABLE_RIGHT_TRICEP + " (" +
            "TIME" + " INTEGER, " +
            "GROUP" + "STRING," +		
            "VALUE"+ " INTEGER);";
    


    TSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(DATABASE_LEFT_BICEP_CREATE);
		 db.execSQL(DATABASE_RIGHT_BICEP_CREATE);
		 db.execSQL(DATABASE_LEFT_TRICEP_CREATE);
		 db.execSQL(DATABASE_RIGHT_TRICEP_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
