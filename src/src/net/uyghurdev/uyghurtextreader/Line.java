package net.uyghurdev.uyghurtextreader;

public class Line {

	private int start;
	private int end;
	private int lIndex;
	private int pIndex;
	private float lWidth;
	private int numOfStretch;
	private String lineString;
	private int y;
	private int x;
	private int kashNum;
	private int parNum;

	public Line() {

	}
	
	public void setParNum(int i){
		parNum=i;
	}
	
	public void setYAxis(int i){
		y=i;
	}
	
	public int getYAxis(){
		return y;
	}

	public void setLineString(String line) {
		lineString = line;
	}

	public String getLineString() {
		return lineString;
	}

	public void setStart(int i) {
		start = i;
	}

	public int getStart() {
		return start;
	}

	public void setEnd(int i) {
		end = i;
	}

	public int getEnd() {
		return end;
	}

	public void setLineIndex(int i) {
		lIndex = i;
	}

	public int getLineIndex() {
		return lIndex;
	}

	public void setPageIndex(int i) {
		pIndex = i;
	}

	public int getPageIndex() {
		return pIndex;
	}

	public void setLineWidth(float f) {
		lWidth = f;
	}

	public float getLineWidth() {
		return lWidth;
	}

	public void setNumOfStrecther(int i) {
		numOfStretch = i;
	}

	public int getNumOfStretcher() {
		return numOfStretch;
	}

	public void setKashNum(int i) {
		// TODO Auto-generated method stub
		kashNum=i;
	}
	
	public int getKashNum(){
		return kashNum;
	}

	public void setXAxis(int i) {
		// TODO Auto-generated method stub
		x=i;
	}
	
	public int getXAxis(){
		return x;
	}
}
