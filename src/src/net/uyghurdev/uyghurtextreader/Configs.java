package net.uyghurdev.uyghurtextreader;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Typeface;

public class Configs {
	public static boolean FontChanged = false;
	public static ArrayList<HashMap<String, String>> Fonts = null;
	public static String FontName="ALKATIP Tor";
	public static String FontColor="BLACK";
	public static String BackGroundColor="WHITE";
	public static String CurrentFile="";
	public static int CurrentPage=0;
	public static int FontSize=20;
	public static int Margin=20;
	public static int PartSeperator=10;
	protected static int FontPosition=0;
	public static Typeface TYPE_FACE;
	public static Typeface UIFont;
	
	
	public static Integer tryParse(String text) {
		  try {
		    return new Integer(text);
		  } catch (NumberFormatException e) {
		    return 10;
		  }
	}
}
