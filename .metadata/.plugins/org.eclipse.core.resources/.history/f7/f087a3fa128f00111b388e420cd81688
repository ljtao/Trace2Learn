package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TraceActivity extends GraphicsActivity{
	
	private int temp = 1;	//hardcode one word only.  Randomize for real later
	
	private TtlView mView;
	private Button backButton;
	private Button prevButton;
	private Button nextButton;
	private TextView tv;
	private String word;
	private static CharDbAdapter mDbHelper;
	private static WordDbAdapter wDbHelper;
	private Canvas c;
	
	private static List<UserCharacter> charlist;
	private static List<String> pathlist;
	private int counter;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning);
        prevButton = (Button)this.findViewById(R.id.prevchar);
        backButton = (Button)this.findViewById(R.id.exitlesson);
        nextButton = (Button)this.findViewById(R.id.nextchar);
        tv = (TextView)this.findViewById(R.id.word);
        mView = (TtlView)this.findViewById(R.id.lessonspace);
        c = new Canvas();
        
        wDbHelper = new WordDbAdapter(this);
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();
        wDbHelper.open();
        
        charlist = buildList();
        
        word = wDbHelper.fetchWord(temp).getString(1);
        tv.setText(word);
        counter = 0;
        
        pathlist = buildPathList(word);   
        mView.setChar(pathlist.get(counter));

        
        
        
        /*
        String fullbmppath = adjustPath(charlist.get(1).getImagePath());
        //Bitmap bmp = BitmapFactory.decodeFile(fullbmppath);
        mView.DrawFromString(charlist.get(1).getPath());
        mView.onDraw(c);
        */
        
        
        prevButton.setOnClickListener(new OnClickListener() {            
        	public void onClick(View v) {
        		if(counter > 0){
        			counter--;
        			mView.clear();
        		
        			mView.setChar(pathlist.get(counter));
        		}
        	}
          }); 
        
        backButton.setOnClickListener(new OnClickListener() {            
            public void onClick(View v) {
           	 Intent myIntent = new Intent(v.getContext(), Main.class);
             startActivityForResult(myIntent, 0);
            }
          }); 
    
        
        nextButton.setOnClickListener(new OnClickListener() {      	
        	  public void onClick(View v) {
        		if(counter < pathlist.size()-1){
        			counter++;
        			mView.clear();
        			mView.setChar(pathlist.get(counter));
        		}
        	}
          });          
    }
    
    private List<UserCharacter> buildList(){
    	int count = mDbHelper.fetchAllChars().getCount();
    	List<UserCharacter> output = new ArrayList<UserCharacter>();
    	for(int i = 1; i<=count; i++){
    		Cursor c = mDbHelper.fetchChar(i);
			//output.add(c.getString(3));	
    		UserCharacter uc = new UserCharacter();
    		uc.setId(c.getInt(0));
    		uc.setName(c.getString(1));
    		uc.setTags(c.getString(2));
    		uc.setImagePath(c.getString(3));
    		uc.setPath(c.getString(4));
    		output.add(uc);
    	}
    	return output;
    	
    }
    
    private String adjustPath(String path){
    	return this.getFilesDir().getAbsolutePath() + "/" + path;
    }
    
    private List<String> buildPathList(String word){
    	//given a randomized word in the lesson, build a list of the paths for each character
    	String s = word;
    	List<String> output = new ArrayList<String>();
    	for(int i = 0; i < charlist.size(); i++){
    		UserCharacter uc = charlist.get(i);
    		if(s.startsWith(uc.getName())){
    			//found character
    			output.add(uc.getPath());
    			int length = uc.getName().length();
    			s = s.substring(length, s.length());
    		}    		
    	}
    	return output;
    }

    

}
