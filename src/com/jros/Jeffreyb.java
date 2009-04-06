package com.jros;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Jeffreyb extends TabActivity implements OnItemSelectedListener {
	Cursor model=null;
	TaskAdapter adapter=null;
    private static final String possibleLocations[] = {"Laptop", "Home", "Work", "Errand"};
	EditText task=null;
	Spinner location=null;
	EditText detail=null;
	SQLiteDatabase db=null;
	Task current=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db=(new TaskSQLiteHelper(this)) .getWritableDatabase(); 
        
        task=(EditText)findViewById(R.id.task); 
        detail=(EditText)findViewById(R.id.detail); 
        location = (Spinner) findViewById(R.id.spinner);
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, possibleLocations);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(spinnerAdapter);
        location.setOnItemSelectedListener(this);
        
        Button save=(Button)findViewById(R.id.save);
        Button delete=(Button)findViewById(R.id.dicon);
        
        save.setOnClickListener(onSave);
        delete.setOnClickListener(onDelete);
        
        ListView list=(ListView)findViewById(R.id.taskList); 
        
        model=Task.getAll(db);
        startManagingCursor(model);
        
        adapter=new TaskAdapter(model);
        list.setAdapter(adapter); 
        
        TabHost.TabSpec spec=getTabHost().newTabSpec("tag1"); 
        spec.setContent(R.id.taskList); 
        spec.setIndicator("Tasks");
        getTabHost().addTab(spec); 
        
        spec=getTabHost().newTabSpec("tag2"); 
        spec.setContent(R.id.options); 
        spec.setIndicator("Add"); 
        getTabHost().addTab(spec); 
        getTabHost().setCurrentTab(1);
        
        list.setOnItemClickListener(onListClick);
    }
  
    @Override 
    public void onDestroy() { 
      super.onDestroy(); 
      
      db.close(); 
    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication()).inflate(R.menu.option, menu);

		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) { 
	  if (item.getItemId()==R.id.delete) { 
	    //startActivity(new Intent(this, DetailForm.class));
	  } 
	  else if (item.getItemId()==R.id.complete) { 
		//startActivity(new Intent(this, EditPreferences.class)); 
	  }  
	  return(super.onOptionsItemSelected(item)); 
	} 
	
    private View.OnClickListener onSave=new View.OnClickListener() { 
    	  public void onClick(View v) { 
		  
		    current=new Task(); 
		    current.setTask(task.getText().toString()); 
		    current.setLocation(possibleLocations[location.getSelectedItemPosition()]); 
		    current.setDetail(detail.getText().toString()); 

		    current.save(db);
		    model.requery();
    	  } 
    	};
    	
    private View.OnClickListener onDelete=new View.OnClickListener() { 
      	  public void onClick(View v) { 
  		  
  		    current=new Task(); 
  		    current.setTask(task.getText().toString()); 
  		    current.setLocation(possibleLocations[location.getSelectedItemPosition()]); 
  		    current.setDetail(detail.getText().toString()); 

  		    if (current.delete(db) != -1){
  	  		    task.setText("");
  	  		    detail.setText("");
  		    }
  		    model.requery();
  		   } 
      	};

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			
    			System.out.println("onItemClick");
    			
    			model.moveToPosition(position);
    			current=new Task().loadFrom(model);
    			
    			task.setText(current.getTask());
    			detail.setText(current.getDetail());
    			
    			if (current.getLocation().equals("Laptop")) {
    				location.setSelection(0);
    			}
    			else if (current.getLocation().equals("Home")) {
    				location.setSelection(1);
    			}
    			else if (current.getLocation().equals("Work")) {
    				location.setSelection(2);
    			}
    			else {
    				location.setSelection(3);
    			}
    			
    			getTabHost().setCurrentTab(1);
    		}
    	}; 	
    	
      public void onItemSelected(AdapterView parent, View v, int position, long id) {
    	  Task t=new Task();
    	  location=(Spinner)findViewById(R.id.spinner);
          t.setLocation(location.getSelectedItem().toString());
      }

      public void onNothingSelected(AdapterView parent) {
      } 
    	
      class TaskAdapter extends CursorAdapter { 
    	  TaskAdapter(Cursor c) { 
    	    super(Jeffreyb.this, c); 
    	  } 
 
    	  public void bindView(View row, Context ctxt, Cursor c) { 
    		TaskWrapper wrapper=(TaskWrapper)row.getTag(); 
    	    wrapper.populateFrom(c); 
    	  } 

    	  public View newView(Context ctxt, Cursor c, ViewGroup parent) { 
    	    LayoutInflater inflater=getLayoutInflater(); 
    	    View row=inflater.inflate(R.layout.row, null); 
    	    TaskWrapper wrapper=new TaskWrapper(row); 
    	    row.setTag(wrapper); 
    	    wrapper.populateFrom(c); 
    	    return(row); 
    	  }
    	} 
    	
    	class TaskWrapper { 
    		  private TextView task=null; 
    		  private TextView location=null;
    		  private TextView detail=null;
    		  private ImageView icon=null;
    		  private View row=null; 
    		  TaskWrapper(View row) { 
    		    this.row=row; 
    		  } 
    		  void populateFrom(Cursor c) { 
    			getTask().setText(c.getString(c.getColumnIndex("task"))); 
    			getLocation().setText(c.getString(c.getColumnIndex("location")));
    			getDetail().setText(c.getString(c.getColumnIndex("detail")));
    			getIcon().setImageResource(R.drawable.arrow);
    		  }
    		  
    		  TextView getTask() { 
    		    if (task==null) { 
    		    	task=(TextView)row.findViewById(R.id.task); 
    		    } 
    		    return(task); 
    		  } 
    		  TextView getLocation() { 
    		    if (location==null) { 
    		    	location=(TextView)row.findViewById(R.id.location); 
    		    } 
    		    return(location); 
    		  }
    		  TextView getDetail() { 
      		    if (detail==null) { 
      		    	detail=(TextView)row.findViewById(R.id.detail); 
      		    } 
      		    return(detail); 
      		  } 
    		  ImageView getIcon() { 
    		    if (icon==null) { 
    		      icon=(ImageView)row.findViewById(R.id.icon); 
    		    } 
    		    return(icon); 
    		  }     		  
    		} 
}