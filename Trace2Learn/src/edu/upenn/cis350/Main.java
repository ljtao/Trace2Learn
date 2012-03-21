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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;



public class Main extends Activity {

    private Button traceButton;
    private Button charactersButton;
    
	//public CharactersDataSource datasource;

    
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main); 
		
        //mView = (TtlView)this.findViewById(R.id.touchpaint);
        traceButton = (Button)this.findViewById(R.id.trace);
        charactersButton = (Button)this.findViewById(R.id.characters);
        
       // mView.setLayoutParams(new LinearLayout.LayoutParams(
       //         LinearLayout.LayoutParams.FILL_PARENT,
       //         (getDisplayHeight()/4)*3));
        
        traceButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
            	 Intent myIntent = new Intent(v.getContext(), TouchPaint.class);
                 startActivityForResult(myIntent, 0);

            }
          });
        
        charactersButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
            	 Intent myIntent = new Intent(v.getContext(), Characters.class);
                 startActivityForResult(myIntent, 0);

            }
          });
    }
    
}
