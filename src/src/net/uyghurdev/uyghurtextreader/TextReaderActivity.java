package net.uyghurdev.uyghurtextreader;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TextReaderActivity extends TabActivity {
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);


			
	        
	        /** TabHost will have Tabs */
	        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

	        
	        /** TabSpec used to create a new tab.
	        * By using TabSpec only we can able to setContent to the tab.
	        * By using TabSpec setIndicator() we can set name to tab. */

	        /** tid1 is firstTabSpec Id. Its used to access outside. */
	        TabSpec open = tabHost.newTabSpec("tid1");
	        TabSpec recent = tabHost.newTabSpec("tid1");
	 
	        

	        /** TabSpec setIndicator() is used to set name for the tab. */
	        /** TabSpec setContent() is used to set content for a particular tab. */
	        open.setIndicator("",getResources().getDrawable(R.drawable.file_open)).setContent(new Intent(this,Open.class));
	        recent.setIndicator("",getResources().getDrawable(R.drawable.history)).setContent(new Intent(this,RecentBooks.class));

	        /** Add tabSpec to the TabHost to display. */
	        
	        tabHost.addTab(open);
	        tabHost.addTab(recent);

	        
	    }
}
