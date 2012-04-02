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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    /** Used as a pulse to gradually fade the contents of the window. */
    private static final int FADE_MSG = 1;
    
    /** Menu ID for the command to clear the window. */
    private static final int CLEAR_ID = Menu.FIRST;
    /** Menu ID for the command to toggle fading. */
    private static final int FADE_ID = Menu.FIRST+1;
    
    /** How often to fade the contents of the window (in ms). */
    private static final int FADE_DELAY = 100;
    
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
    
    
    //private CharacterOpenHelper opener;
	//public CharactersDataSource datasource;

	
	private CharDbAdapter mDbHelper;
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trace); 
        
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();
        //mDbHelper.createChar("testname123", "tags,tags,tags123", "testfile.file123");
        
        
        mView = (TtlView)this.findViewById(R.id.touchpaint);
        saveButton = (Button)this.findViewById(R.id.save);
        backButton = (Button)this.findViewById(R.id.back);
        clearButton = (Button)this.findViewById(R.id.clear);

        //TODO: Clear button and listeneter
        
        //db = new ArrayList<UserCharacter>();
        
       // datasource = new CharactersDataSource(this);
		//datasource.open();
        
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
          
         
          
       
        mFading = savedInstanceState != null ? savedInstanceState.getBoolean("fading", true) : true;
    }
    
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, CLEAR_ID, 0, "Clear");
        menu.add(0, FADE_ID, 0, "Fade").setCheckable(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(FADE_ID).setChecked(mFading);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CLEAR_ID:
                mView.clear();
                return true;
            case FADE_ID:
                mFading = !mFading;
                if (mFading) {
                    startFading();
                } else {
                    stopFading();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onResume() {
        super.onResume();
        // If fading mode is enabled, then as long as we are resumed we want
        // to run pulse to fade the contents.
        if (mFading) {
            //startFading();
        	//uncomment to enable fading
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save away the fading state to restore if needed later.  Note that
        // we do not currently save the contents of the display.
        outState.putBoolean("fading", mFading);
    }

    @Override protected void onPause() {
        super.onPause();
        // Make sure to never run the fading pulse while we are paused or
        // stopped.
        stopFading();
    }

    /**
     * Start up the pulse to fade the screen, clearing any existing pulse to
     * ensure that we don't have multiple pulses running at a time.
     */
    void startFading() {
        mHandler.removeMessages(FADE_MSG);
        mHandler.sendMessageDelayed(
                mHandler.obtainMessage(FADE_MSG), FADE_DELAY);
    }
    
    /**
     * Stop the pulse to fade the screen.
     */
    void stopFading() {
        mHandler.removeMessages(FADE_MSG);
    }
    
    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                // Upon receiving the fade pulse, we have the view perform a
                // fade and then enqueue a new message to pulse at the desired
                // next time.
                case FADE_MSG: {
                    mView.fade();
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(FADE_MSG), FADE_DELAY);
                    break;
                }
                default:
                    super.handleMessage(msg);
            }
        }
    };
    
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
        ad.setTitle( title );
        ad.setMessage( message );
        ad.setView(input);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1){
            	
            	mView.setDrawingCacheEnabled(true);
            	Bitmap b = mView.getDrawingCache();
            	String value = input.getText().toString().trim();
            	FileOutputStream fos;
				try {
					String fname = value + ".png";
					mDbHelper.createChar(value, "", fname);
					fos = openFileOutput(fname, Context.MODE_PRIVATE);
	            	b.compress(Bitmap.CompressFormat.PNG, 100, fos);
	        		fos.close();
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


        		
            	//UserCharacter uc = new UserCharacter(b, value);            	
            	//add to "database"           	
            	//db.add(uc);
            	
            	
            	mView.clear();
            	
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //nothing
            }

			public void onClick(View arg0) {
				
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
                mDbHelper.createChar(name, tags, file);
                
                break;
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
