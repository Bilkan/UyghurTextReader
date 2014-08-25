package net.uyghurdev.uyghurtextreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Page {

	private List<Line> _line;
	private int pIndex;
	
	public Page (){
		_line=new ArrayList<Line>();
	}
	
	public void addLine(Line line){
		_line.add(line);
	}
	
	public List<Line> getPageLines(){
		return _line;
	}
	
	public void addIndex(int i){
		pIndex=i;
	}
	
	public int getIndex(){
		return pIndex;
	}
}
