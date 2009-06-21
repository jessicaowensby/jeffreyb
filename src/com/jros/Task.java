package com.jros;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mdt.rtm.ApplicationInfo;
import com.mdt.rtm.Invoker;
import com.mdt.rtm.ServiceException;
import com.mdt.rtm.ServiceImpl;
import com.mdt.rtm.data.RtmAuth;
import com.mdt.rtm.data.RtmFrob;
import com.mdt.rtm.data.RtmTask;
import com.mdt.rtm.data.RtmTasks;
import com.mdt.rtm.data.RtmUser;

public class Task { 
  private String task=""; 
  private String location=""; 
  private String detail="";
  
  public String getTask() { 
    return(task); 
  } 
  
  public void setTask(String task) { 
    this.task=task; 
  } 
  
  public String getLocation() { 
    return(location); 
  } 
  
  public void setLocation(String location) { 
    this.location=location; 
  } 

  public String getDetail() { 
	return(detail); 
  } 
	  
  public void setDetail(String detail) { 
	this.detail=detail; 
   }
  
  void save(SQLiteDatabase db) {
	  
	  Cursor c =db.rawQuery("SELECT _id FROM tasks WHERE task = '" + getTask() + "'", null);
	  ContentValues cv=new ContentValues(); 
	  cv.put("task", task); 
	  cv.put("location", location); 
	  cv.put("detail", detail); 
	  
	  if (c.getCount() == 0){
		  db.insert("tasks", "detail", cv); 
	  } else {
		  c.moveToFirst();
		  String _id = c.getString(c.getColumnIndex("_id"));
		  String[] id = {_id};
		  db.update("tasks", cv, "_id=?", id);
	  }
	}
  
  int delete(SQLiteDatabase db) {
	  return(db.delete("tasks", "task" + "= '" + getTask() + "'", null));
	}
  
  static Cursor getAll(SQLiteDatabase db) { 
	  return(db.rawQuery("SELECT * FROM tasks ORDER BY task", null)); 
  }
  
  Task loadFrom(Cursor c) { 	  
	  task=c.getString(c.getColumnIndex("task")); 
	  location=c.getString(c.getColumnIndex("location")); 
	  detail=c.getString(c.getColumnIndex("detail")); 
	  return(this); 
	} 
} 
