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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.


/**
 * Demonstrates the handling of touch screen and trackball events to
 * implement a simple painting app.
 */
public class TouchPaint extends GraphicsActivity {

    
    /** Other elements on Screen */
    private Button saveButton;
    private Button backButton;
    private Button clearButton;
        
    /** The view responsible for drawing the window. */
    TtlView mView;
    ScrollView master_view;
    LinearLayout primary_layout;
    /** Is fading mode enabled? */
    boolean mFading;
    
    String paths;
    
	
	private CharDbAdapter mDbHelper;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trace); 
        
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
         String ctd = extras.getString("charToDraw");
         
       	 Log.d("extrasTP", ctd);		           	 
         //String rowid = (charToDraw);
         Cursor c = mDbHelper.fetchCharByName(ctd);
         paths = c.getString(4);
         String name = c.getString(1);

         String str;
         
        }


        
   
        
        
        mView = (TtlView)this.findViewById(R.id.touchpaint);
        
        mView.setChar(paths);
        saveButton = (Button)this.findViewById(R.id.save);
        backButton = (Button)this.findViewById(R.id.back);
        clearButton = (Button)this.findViewById(R.id.clear);
        
        
        
    


        
        mView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                (getDisplayHeight()/4)*3));
        
        saveButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
            	savePopUp(R.string.save_popup_title, R.string.save_popup_prompt);

            }
          });
        
        backButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
           	 Intent myIntent = new Intent(v.getContext(), Main.class);
             startActivityForResult(myIntent, 0);

            }
          });
        
        clearButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
           	 mView.clear();
            

            }
          });
          
         
        

        
        
          
       
       
    }
    





    
    private int getDisplayHeight(){
    	Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
    		return display.getHeight();
    }
    
    public void createChar(String name, String tags, String fname) {
    	Intent i = new Intent(this, TouchPaint.class);
        startActivityForResult(i, 1);
    }
    
    public void savePopUp(int title,int message ){
        final EditText input = new EditText(this);
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        mDbHelper.open();
        ad.setTitle( title );
        ad.setMessage( message );
        ad.setView(input);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1){
            	
            	mView.setDrawingCacheEnabled(true);
            	String path = mView.getLineListAsString();
            	
            	Bitmap b = mView.getDrawingCache();
            	String value = input.getText().toString().trim();
            	FileOutputStream fos;
				try {
					String fname = value + ".png";
					mDbHelper.createChar(value, "", fname, path);
					fos = openFileOutput(fname, Context.MODE_PRIVATE);
	            	b.compress(Bitmap.CompressFormat.PNG, 100, fos);
	        		fos.close();
	        		mDbHelper.close();
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mDbHelper.close();
					e.printStackTrace();
				}


        		
            	//UserCharacter uc = new UserCharacter(b, value);            	
            	//add to "database"           	
            	//db.add(uc);
            	
            	mView.clear();
            	mView.destroyDrawingCache();

            	
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                mDbHelper.close();  
            }

        }).show();
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case 1:
                String name = extras.getString(CharDbAdapter.KEY_NAME);
                String tags = extras.getString(CharDbAdapter.KEY_TAGS);
                String file = extras.getString(CharDbAdapter.KEY_FILE);
                String path = extras.getString(CharDbAdapter.KEY_PATH);
                mDbHelper.createChar(name, tags, file, path);
                
                break;
            case 2:
            	mView.testDraw();
            	Toast.makeText(this, "test case 2", Toast.LENGTH_LONG).show();
            /*case ACTIVITY_EDIT:
                Long rowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
                if (rowId != null) {
                    String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
                    String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
                    mDbHelper.updateNote(rowId, editTitle, editBody);
                }
                fillData();
                break;
                */
        }
    }

}
