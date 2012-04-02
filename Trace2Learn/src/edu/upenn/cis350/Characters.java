/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.upenn.cis350;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.




/**
 * Demonstrates the handling of touch screen and trackball events to
 * implement a simple painting app.
 */
public class Characters extends ListActivity {
    /** Used as a pulse to gradually fade the contents of the window. */
   
    
    ListView lv;   
    private Button backButton;
    private EfficientAdapter adap;
    private static CharDbAdapter mDbHelper;
    
    //private static String[] data = new String[] {CharDbAdapter.KEY_FILE, CharDbAdapter.KEY_NAME };
    private static String[] data;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characters);                
        backButton = (Button)this.findViewById(R.id.back2);   
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();
        
        data = buildArray();


        adap = new EfficientAdapter(this);
        setListAdapter(adap);
       
        
        backButton.setOnClickListener(new OnClickListener() {        
            public void onClick(View v) {
           	 Intent myIntent = new Intent(v.getContext(), Main.class);
             startActivityForResult(myIntent, 0);
            }
        });
          

    }
    
    
    private static String[] buildArray(){
    	int count = mDbHelper.fetchAllChars().getCount();
    	String[] output = new String[count];
    	for(int i = 0; i<count; i++){
    		output[i] = Integer.toString(i+1);
    	}
    	return output;
    	
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllChars();
        startManagingCursor(c);

        String[] from = new String[] {CharDbAdapter.KEY_FILE, CharDbAdapter.KEY_NAME };
        int[] to = new int[] {R.id.icon, R.id.text1 };
       
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.char_row, c, from, to);        
        lv.setAdapter(notes);
        
    }
    
    private String pathToBMP(String filename){
    	return "/files/" + filename;
    }
    
    
    
    
    
    public static class EfficientAdapter extends BaseAdapter {
    	private LayoutInflater mInflater;
    	private Bitmap mIcon1;
    	private Context context;
    	 
    	public EfficientAdapter(Context context) {
    	// Cache the LayoutInflate to avoid asking for a new one each time.
    	mInflater = LayoutInflater.from(context);
    	this.context = context;
    	}
    	 
    	/**
    	* Make a view to hold each row.
    	*
    	* @see android.widget.ListAdapter#getView(int, android.view.View,
    	*      android.view.ViewGroup)
    	*/
    	public View getView(final int position, View convertView, ViewGroup parent) {
    	// A ViewHolder keeps references to children views to avoid
    	// unneccessary calls
    	// to findViewById() on each row.
    	ViewHolder holder;
    	 
    	// When convertView is not null, we can reuse it directly, there is
    	// no need
    	// to reinflate it. We only inflate a new View when the convertView
    	// supplied
    	// by ListView is null.
    	if (convertView == null) {
    	convertView = mInflater.inflate(R.layout.char_row, null);
    	 
    	// Creates a ViewHolder and store references to the two children
    	// views
    	// we want to bind data to.
    	holder = new ViewHolder();
    	holder.textLine = (TextView) convertView.findViewById(R.id.text1);
    	holder.iconLine = (ImageView) convertView.findViewById(R.id.icon);
    	 
    	  
    	convertView.setTag(holder);
    	} else {
    	// Get the ViewHolder back to get fast access to the TextView
    	// and the ImageView.
    	holder = (ViewHolder) convertView.getTag();
    	}
    	 
    	// POPULATE ROWS
    	Cursor c = mDbHelper.fetchChar(position+1);
    	String filename = c.getString(1);
    	
    	// Icons bound to the rows.
    	//String fname = context.getFilesDir().getAbsolutePath()+ "/" + c.getString(3);
    	String fname = context.getFilesDir().getAbsolutePath()+ "/" + c.getString(3);
    	mIcon1 = BitmapFactory.decodeFile(fname);
    	 
    	// Bind the data efficiently with the holder.
    	holder.iconLine.setImageBitmap(mIcon1);
    	holder.textLine.setText(filename);
    	 
    	return convertView;
    	}
    	 
    	static class ViewHolder {
    	TextView textLine;
    	ImageView iconLine;
    	Button buttonLine;
    	}

    	 
    	@Override
    	public long getItemId(int position) {
    	// TODO Auto-generated method stub
    	return 0;
    	}
    	 
    	@Override
    	public int getCount() {
    	// TODO Auto-generated method stub
    	return data.length;
    	}
    	 
    	@Override
    	public Object getItem(int position) {
    	// TODO Auto-generated method stub
    	return data[position];
    	}
    	 
    }
}
