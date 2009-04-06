package com.jros;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException; 
import android.database.sqlite.SQLiteOpenHelper; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteQueryBuilder; 

class TaskSQLiteHelper extends SQLiteOpenHelper { 
  private static final String DATABASE_NAME="jeffreyb.db"; 
  private static final int SCHEMA_VERSION=1; 
  
  public TaskSQLiteHelper(Context context) { 
    super(context, DATABASE_NAME, null, SCHEMA_VERSION); 
  } 
  
  @Override 
  public void onCreate(SQLiteDatabase db) {
	  Cursor c=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tasks'", null);
	  
	  try{
		    if (c.getCount()==0) { 
		        db.execSQL("CREATE TABLE tasks (_id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, location TEXT, detail TEXT);"); 
		      } 
	  }
	  finally{
		  c.close();
	  }
  } 
  @Override 
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  db.execSQL("DROP TABLE IF EXISTS tasks"); 
	  onCreate(db);
  } 
}