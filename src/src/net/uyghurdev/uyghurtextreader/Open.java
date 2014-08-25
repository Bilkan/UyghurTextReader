package net.uyghurdev.uyghurtextreader;



import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Open extends Activity {
    /** Called when the activity is first created. */
	View open;
	ArrayList<HashMap<String,String>> saFiles;
	public File CurrentDirectory=new File("/");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		LayoutInflater inflater = LayoutInflater.from(this);
		open = inflater.inflate(R.layout.open, null);
		
        Configs.UIFont = Typeface.createFromAsset(getAssets(), "tor.ttf");
//        TextView title = (TextView)findViewById(R.id.title);
//		title.setTypeface(Configs.UIFont);
//		title.setText(ArabicUtilities.reshape(getString(R.string.app_name)));
       
        
		showSDCard();
	
        ImageButton ibHome=(ImageButton)open.findViewById(R.id.ibInnerMem);
        ibHome.setOnClickListener(new ImageButton.OnClickListener(){
        	public void onClick(View v)
        	{
        		showRoot();
        	}
        });
        
        ImageButton ibSDCard=(ImageButton)open.findViewById(R.id.ibOuterMem);
        ibSDCard.setOnClickListener(new ImageButton.OnClickListener(){
        	public void onClick(View v)
        	{
        		showSDCard();
        	}
        });
        
        ImageButton ibUpOneLevel=(ImageButton)open.findViewById(R.id.ibBack);
        ibUpOneLevel.setOnClickListener(new ImageButton.OnClickListener(){
        	public void onClick(View v)
        	{
        		upOneLevel();
        	}
        });
        final ListView lvFiles=(ListView)open.findViewById(R.id.lvFiles);
        lvFiles.setCacheColorHint(0);
        lvFiles.setOnItemClickListener(new OnItemClickListener() {
        	 @Override
        	 public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		 File SelectedFile;
        		 if(CurrentDirectory.getParent()!=null)
	        	 {
        			 SelectedFile=new File(CurrentDirectory.getAbsolutePath()+"/"+saFiles.get(position).get("path"));
	        	 }
        		 else
        		 {
        			 SelectedFile=new File(CurrentDirectory.getAbsolutePath()+saFiles.get(position).get("path"));
        		 }
	        	 
	        	 if(SelectedFile.isDirectory())
	        	 {
	        		 showDirectory(SelectedFile);
	        	 }
	        	 else
	        	 {
	        		 if(SelectedFile.getAbsolutePath().toLowerCase().endsWith(".txt"))
	        		 {
	        			 openFile(SelectedFile);
	        		 }
	        		 else
	        		 {
		        		 AlertDialog.Builder adb=new AlertDialog.Builder(Open.this);
			        	 adb.setTitle("Open File");
			        	 adb.setMessage("Selected Item is = "+SelectedFile.getAbsolutePath());
			        	 adb.setPositiveButton("Ok", null);
			        	 adb.show();
	        		 }
	        	 }
        	 }

			
        });

        setContentView(open);
    }
    
    private void openFile(File selectedFile) {
    	
    	String filePath = selectedFile.getAbsolutePath();
    	Database data = new Database(this);
    	Cursor recent = data.getRecent(filePath);
    	Calendar calendar=Calendar.getInstance();
		long time = calendar.getTimeInMillis();
    	if(recent.getCount()<=0){
    		data.addRecent(filePath, selectedFile.getName(), time);
    	}else{
    		recent.moveToFirst();
    		data.changeTime(filePath, time);
    	}
    	
    	Configs.CurrentFile=filePath;
		Intent newIntent=new Intent();
		newIntent.setClass(Open.this, Text.class);
		//newIntent.putExtra("FilePath", selectedFile.getAbsolutePath());
		startActivity(newIntent);
		
	}
	
	
	public void showRoot()
	{
		showDirectory(new File("/"));
	}
	
	
	

	public void showSDCard()
	{
		if(isSdPresent()){
			showDirectory(new File("/sdcard/"));
		}else{
			showRoot();
		}
		
	}
	
	private void showDirectory(File fl) {
		
		if(fl.isDirectory())
		{
			CurrentDirectory= fl;
			File[] files=fl.listFiles();
			saFiles=new ArrayList<HashMap<String,String>>();
			HashMap<String,String> map;
			for(int nIndex=0;nIndex<files.length;nIndex++)
			{
				if(files[nIndex].isDirectory()||files[nIndex].getName().endsWith(".txt")){
					map=new HashMap<String,String>();
					map.put("path", files[nIndex].getName());
					saFiles.add(map);
				}
			}
			
			Collections.sort(saFiles, sDisplayNameComparator);
		
			FileAdapter aa=new FileAdapter(this,saFiles);
			ListView lvFiles=(ListView)open.findViewById(R.id.lvFiles);
			lvFiles.setAdapter(aa);
		}
	}
	
	private final static Comparator<Map> sDisplayNameComparator = new Comparator<Map>() {
        private final Collator   collator = Collator.getInstance();

        public int compare(Map map1, Map map2) {
            return collator.compare(map1.get("path"), map2.get("path"));
        }
    };
	
	public static boolean isSdPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	private void upOneLevel()
	{
		if(this.CurrentDirectory.getParent()!=null)
		{
			this.showDirectory(this.CurrentDirectory.getParentFile());
		}
		
	}
   
	}
    
