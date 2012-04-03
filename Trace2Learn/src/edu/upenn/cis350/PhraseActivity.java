package edu.upenn.cis350;

import java.io.FileOutputStream;
import java.util.ArrayList;

import edu.upenn.cis350.Characters.EfficientAdapter;
import edu.upenn.cis350.Characters.EfficientAdapter.ViewHolder;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PhraseActivity extends ListActivity {

	private Button backButton;
	ListView lv;
	private ArrayList<Integer> currPhraseCache;
	private TextView tv;
	
    private EfficientAdapter adap;
	private static CharDbAdapter mDbHelper;
	private static WordDbAdapter wDbHelper;
	
    private static String[] data;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phrase);
        currPhraseCache = new ArrayList<Integer>();
        backButton = (Button)this.findViewById(R.id.back3);
        tv = (TextView)this.findViewById(R.id.phraseview);
        
        wDbHelper = new WordDbAdapter(this);
        mDbHelper = new CharDbAdapter(this);
        mDbHelper.open();
        wDbHelper.open();
        
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
	
    
    private String[] buildArray(){
    	int count = mDbHelper.fetchAllChars().getCount();
    	String[] output = new String[count];
    	for(int i = 0; i<count; i++){
    		output[i] = Integer.toString(i+1);
    	}
    	return output;
    	
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String character = (String) adap.getName(position);
	    tv.append(character);
	}
	
   public static class EfficientAdapter extends BaseAdapter implements Filterable {
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
    	//convertView.setClickable(true);
    	//convertView.setOnClickListener(clickListener);
    	 
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
    	  	
		public String getName(int arg0) {  //TODO: customized implementation
			Cursor c = mDbHelper.fetchChar(arg0+1);
			return c.getString(1);			
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
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			return null;
		}


    	 
    }
	
	public boolean onContextItemSelected(final MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = 
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final EditText input = new EditText(this);
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
       // ad.setTitle( title );
        //ad.setMessage( message );
        ad.setView(input);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1){
            	currPhraseCache.add(item.getItemId());
            	
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //nothing
            }

			public void onClick(View arg0) {
				
			}
        }).show();
        
       return true; 
        
        
     
   }
	
	
	
	
}