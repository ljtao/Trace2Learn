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
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.




/**
 * Demonstrates the handling of touch screen and trackball events to
 * implement a simple painting app.
 */
public class Characters extends Activity {
    /** Used as a pulse to gradually fade the contents of the window. */
   
    
    ListView lv;
    
    private Button backButton;
    //private CharactersDataSource datasource;
    
    private CharDbAdapter mDbHelper;
    
    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.characters); 
        
        backButton = (Button)this.findViewById(R.id.back2);
        
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();

        lv = (ListView)findViewById(R.id.list);
        //ArrayList<UserCharacter> charList = TouchPaint.db;
        ArrayList<String> charStringList = new ArrayList<String>();
        
        fillData();
       // for (UserCharacter uc : charList) {
        //	charStringList.add(uc.getChar());
        //}
       //charStringList.add("blah1");
       //charStringList.add("blah2");
       //charStringList.add("blah3");
        //datasource = new CharactersDataSource(this);
		//datasource.open();
        
       //List<UserCharacter> temp = datasource.getAllCharacters();
       //for(int i = 0; i < temp.size(); i++){
    	//   charStringList.add(temp.get(i).getName());
       //}
        
        
        

  
        backButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
           	 Intent myIntent = new Intent(v.getContext(), Main.class);
             startActivityForResult(myIntent, 0);

            }
          });
          

    }
    


    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllChars();
        startManagingCursor(c);

        String[] from = new String[] { CharDbAdapter.KEY_NAME };
        int[] to = new int[] { R.id.list };
        
        // Now create an array adapter and set it to display using our row
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, from);
        lv.setAdapter(arrayAdapter);
        
    }
    

    

}
