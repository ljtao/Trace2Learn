package edu.upenn.cis350;

import java.util.ArrayList;

public class Line {
	
	private ArrayList<Point> pointList;
	
	public Line() {
		pointList = new ArrayList<Point>();
		
	}
	
	public void addPoint(Point p) {
		pointList.add(p);
	}
	
	public ArrayList<Point> getPointList() {
		return pointList;
	}
	
	public Point getLastPoint() {
		if (pointList.size() != 0) {
			return pointList.get(pointList.size() -1);
		}
		else {
			return null;
		}
	}

	public String toString() {
		String ret = "";
		boolean b = false;
		for (Point p : pointList) {
			if (b == true) {
				ret+="|";
				
			}
			b=true;
			ret+=p.toString();
		}
		ret+="";
		return ret;
	}
}
