package edu.upenn.cis350;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;

public class UserCharacter {
	
	private Bitmap image;
	private String letter;
	private List<String> tags;
	
	public UserCharacter(Bitmap b, String s){
		tags = new LinkedList<String>(); 
		image = b;
		letter = s;
	}
	
	protected void addTag(String tag){
		tags.add(tag);
	}

}
