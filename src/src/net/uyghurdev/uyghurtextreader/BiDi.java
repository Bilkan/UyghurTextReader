package net.uyghurdev.uyghurtextreader;

import java.util.ArrayList;

import android.util.Log;

public class BiDi {
	
	ArrayList<Word> words;
	
//	public static char[] RTL = {};

	BiDi(){
		
	}
	
	
	public String reverse(String line){
		
		words = getWords(line);
		words = getProperties(words);
		words = reverseWords(words);
		ArrayList<String> wordarray = setWordPosition(words);
		String revline = getLineString(wordarray);
		return revline;
		
	}
	
	
	private ArrayList<String> setWordPosition(ArrayList<Word> array) {
		// TODO Auto-generated method stub
		ArrayList<String> newArray = new ArrayList<String>();
		int ltrNum = 0;
		for (int p=0; p<array.size(); p++){
//			Log.d("Direction", ""+array.get(array.size()-1-p).Di);
//			Log.d("Direction", ""+array.get(array.size()-1-p).Word);
			if (array.get(array.size()-1-p).Di == 0 || array.get(array.size()-1-p).Di == 20){
				newArray.add(array.get(array.size()-1-p).reWord);
				ltrNum = 0;
			} else if (array.get(array.size()-1-p).Di == 1 || (array.get(array.size()-1-p).Di == 3 && !afterRTL(array, p))){
				newArray.add(p - ltrNum, array.get(array.size()-1-p).Word);
				ltrNum ++;
			} else if (array.get(array.size()-1-p).Di == 3 && afterRTL(array, p)){
				newArray.add(array.get(array.size()-1-p).reWord);
				ltrNum = 0;
			}else if (array.get(array.size()-1-p).Di == 21){
				newArray.add(p - ltrNum, array.get(array.size()-1-p).reWord);
				ltrNum ++;
			}
		}
		return newArray;
	}


	private boolean afterRTL(ArrayList<Word> array, int p) {
		// TODO Auto-generated method stub
		int n=0;
//		boolean punc = true;
		while (true){
			
//			Log.d("n", ""+n);
//			Log.d("p", ""+p);
//			Log.d("Index Bound", ""+(array.size()-1));
			if (array.size()-1-p-n<=0){
//				punc = false;
				break;
			}else{
				if((array.get(array.size()-1-p-n).Di == 1) || (array.get(array.size()-1-p-n).Di == 0) || (array.get(array.size()-1-p-n).Di == 20) || (array.get(array.size()-1-p-n).Di == 21)){
//					punc = false;
					break;
				}
			}
			n++;
		}
		if (array.get(array.size()-1-p-n).Di == 1 || array.get(array.size()-1-p-n).Di == 21){
			return false;
		}else{
			return true;
		}
		
	}


	private String getLineString(ArrayList<String> array) {
		// TODO Auto-generated method stub
		String rev = "";
		for (int s=0; s<array.size(); s++){
			rev += array.get(s);
		}
		
		return rev;
	}


	private ArrayList<Word> reverseWords(ArrayList<Word> array) {
		// TODO Auto-generated method stub
		
		for (int r=0; r<array.size(); r++){
			if (array.get(r).Di != 1 && array.get(r).Di != 2){
				String rev = "";
				for (int rw=0; rw<array.get(r).Word.length(); rw++){
					if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == ')'){
						rev += "(";
					}else 
						if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '('){
						rev += ")";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == ']'){
						rev += "[";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '['){
						rev += "]";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '}'){
						rev += "{";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '{'){
						rev += "}";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '»'){
						rev += "«";
					}else if (array.get(r).Word.charAt(array.get(r).Word.length() - 1 - rw) == '«'){
						rev += "»";
					}else{
						rev += array.get(r).Word.substring(array.get(r).Word.length() - 1 - rw, array.get(r).Word.length() - rw);
					}
				}
				array.get(r).reWord = rev;
			} else if (array.get(r).Di == 2){
				int b = 3;
				int in =0;
				while (b == 3){
					b = isBlongTo(array.get(r).Word.charAt(in));
					in++;
				}
				if (b==0){
					array.get(r).Di = 20;
				}else if(b==1){
					array.get(r).Di = 21;
				}
				array.get(r).reWord = reverseRL(array.get(r).Word, b);
			}
		}
		return array;
	}


	private String reverseRL(String word, int b) {
		// TODO Auto-generated method stub
		ArrayList<String> newArray = new ArrayList<String>();
		String revword = "";
		if (b == 0){
			int ltrNum = 0;
			for (int rev=0; rev<word.length(); rev++){
				if (isBlongTo(word.charAt(word.length()-1-rev)) == 0){
					newArray.add(word.substring(word.length()-1-rev, word.length()-rev));
				}else if (isBlongTo(word.charAt(word.length()-1-rev)) == 1){
					newArray.add(rev - ltrNum, word.substring(word.length()-1-rev, word.length()-rev));
					ltrNum++;
				}else{
					if (afterR(word, rev)){
						newArray.add(word.substring(word.length()-1-rev, word.length()-rev));
					}else if(!afterR(word, rev)){
						newArray.add(rev - ltrNum, word.substring(word.length()-1-rev, word.length()-rev));
						ltrNum++;
					}
				}
			}
		}else if(b == 1){
			int rtlNum = 0;
			for (int rev=0; rev<word.length(); rev++){
				if (isBlongTo(word.charAt(rev)) == 1){
					newArray.add(word.substring(rev, rev + 1));
				}else if (isBlongTo(word.charAt(rev)) == 0){
					newArray.add(rev - rtlNum, word.substring(rev, rev + 1));
					rtlNum++;
				}else{
					if (afterL(word, rev)){
						newArray.add(word.substring(rev, rev+1));
					}else if(!afterL(word, rev)){
						newArray.add(rev - rtlNum, word.substring(rev, rev+1));
						rtlNum++;
					}
				}
			}
		}
		for (int l=0; l<newArray.size(); l++){
			revword += newArray.get(l);
		}
		
		return revword;
	}


	private boolean afterL(String word, int p) {
		// TODO Auto-generated method stub
		int n=0;
//		boolean punc = true;
		while (true){
			
			if (p-n<=0){
//				punc = false;
				break;
			}else{
				if((isBlongTo(word.charAt(p-n)) == 1) || (isBlongTo(word.charAt(p-n)) == 0)){
//					punc = false;
					break;
				}
			}
			n++;
		}
		if (isBlongTo(word.charAt(p-n)) == 0){
			return false;
		}else{
			return true;
		}
	}


	private boolean afterR(String word, int p) {
		// TODO Auto-generated method stub
		int n=0;
//		boolean punc = true;
		while (true){
			
			if (word.length()-1-p-n<=0){
//				punc = false;
				break;
			}else{
				if((isBlongTo(word.charAt(word.length()-1-p-n)) == 1) || (isBlongTo(word.charAt(word.length()-1-p-n)) == 0)){
//					punc = false;
					break;
				}
			}
			n++;
		}
		if (isBlongTo(word.charAt(word.length()-1-p-n)) == 1){
			return false;
		}else{
			return true;
		}
	}


	private ArrayList<Word> getProperties(ArrayList<Word> array) {
		// TODO Auto-generated method stub
		for (int w=0; w<array.size();w++){
			String type = ""; // = new int[array.get(w).Word.length()]; // true:rtl; false:ltr
			boolean end;   // true:rtl; false:ltr
			for(int c=0; c<array.get(w).Word.length();c++){
				type += isBlongTo(array.get(w).Word.charAt(c));
			}
			if (type.contains("0") && !type.contains("1")){
				array.get(w).Di = 0;
			}else if(type.contains("1") && !type.contains("0")){
				array.get(w).Di = 1;
			}else if (type.contains("0") && type.contains("1")){
				array.get(w).Di = 2;
			}else{
				array.get(w).Di = 3;
			}
		}
		
		
		return array;
	}


	private int isBlongTo(char chr) {
		// TODO Auto-generated method stub
		int b=0;
		if ((chr>=1536 && chr<=1631) || ((chr>=1643 && chr<=1791))) {
			b=0;  //rtl
		}else if(chr == 32 || chr == 44 || chr == 46 || chr == 63 || chr == 33 || chr == 34 || chr == 40 || chr == 41 || chr == 91 || chr == 93 || chr == 123 || chr == 125 || chr == 39 || chr == 171 || chr == 187){
			b=3;  //punc
		}else{
			b=1;  //ltr
		}
		
		return b;
	}


	private ArrayList<Word> getWords(String line) {
		// TODO Auto-generated method stub
		ArrayList<Word> array = new ArrayList<Word>();
		int n = 0;
		Word word = new Word();
		while(n<line.length()){
//			Log.d("Letter", line.substring(n, n+1));
//			Log.d("Word", word.Word);
			word.Word += line.substring(n, n+1);
			if (line.charAt(n) == ' ' 
					|| line.charAt(n) == ','
					|| line.charAt(n) == '.'
					|| line.charAt(n) == '?'
					|| line.charAt(n) == '!'
					|| line.charAt(n) == '،'
					|| line.charAt(n) == '؟'
					|| line.charAt(n) == '.'
//					|| line.charAt(n) == '"'
//					|| line.charAt(n) == ')'
//					|| line.charAt(n) == '('
//					|| line.charAt(n) == ']'
//					|| line.charAt(n) == '['
//					|| line.charAt(n) == '{'
//					|| line.charAt(n) == '}'
//					|| line.charAt(n) == '»'
//					|| line.charAt(n) == '«'
//					|| line.charAt(n) == '\''
						){
				array.add(word);
				word =new Word();
			}else if(n == line.length()-1){
				array.add(word);
			}
			n++;
		}
		return array;
	}


	class Word {
		int Di = 0;  //0:rtl; 1:ltr; 2:r&l; 3:punc; 
		String Word = "";
		String reWord = "";
	}
}
