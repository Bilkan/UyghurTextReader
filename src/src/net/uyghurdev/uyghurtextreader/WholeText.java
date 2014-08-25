package net.uyghurdev.uyghurtextreader;

import java.util.ArrayList;

import android.util.Log;

public class WholeText {

	
	private String title = "";
	private String text = "";
	private ArrayList<Page> pages;
	private ArrayList<String> paragraphs;

	public WholeText() {
		pages=new ArrayList<Page>();
		paragraphs=new ArrayList<String>();
	}
	
	public String getParagraph(int i){
		return paragraphs.get(i);
	}
	
	public void addParagraph(String prgrph){
		paragraphs.add(prgrph);
	}

	public int getPageNum(){
		return pages.size();
	}
	public void addPage(Page page){
		pages.add(page);
	}

	public Page getPage(int order){
		return pages.get(order);
	}
	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public void setText(String theString) {
		// TODO Auto-generated method stub
		Log.d("My log", "Add text");
		text = text + theString;
	}
//
//	public void setEm(String theString) {
//		// TODO Auto-generated method stub
//		text = text + "\nem    " + theString;
//	}
//
//	public void setDiv(String theString) {
//		// TODO Auto-generated method stub
//		text = text + "\ndiv    " + theString;
//	}
//
//	public void setLi(String theString) {
//		// TODO Auto-generated method stub
//		text = text + "\nli    " + theString;
//	}
//
//	public void setA(String theString) {
//		// TODO Auto-generated method stub
//		text = text + "\na    " + theString;
//	}
//
//	public void setStrong(String theString) {
//		// TODO Auto-generated method stub
//		text = text + "\nstrong    " + theString;
//	}

	public void setTitle(String theString) {
		// TODO Auto-generated method stub
		Log.d("My log", "Add title");
		title = title + theString;
	}

	public int getParSize() {
		
		return paragraphs.size();
	}


//	public void setB(String theString) {
//		// TODO Auto-generated method stub
//		title = title + "\nb    " + theString;
//	}
//
//	public void setSpan(String theString) {
//		// TODO Auto-generated method stub
//		title = title + "\nspan    " + theString;
//	}
//
//	public void setH3(String theString) {
//		// TODO Auto-generated method stub
//		title = title + "\nh3    " + theString;
//	}
//
//	public void setH2(String theString) {
//		// TODO Auto-generated method stub
//		title = title + "\nh2    " + theString;
//	}
//
//	public void setH1(String theString) {
//		// TODO Auto-generated method stub
//		title = title + "\nh1    " + theString;
//	}

}
