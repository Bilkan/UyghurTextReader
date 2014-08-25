package net.uyghurdev.uyghurtextreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecentBooks extends Activity {

	Database data;
	Cursor books;
	ArrayList<HashMap<String, String>> recentbooks;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View recent = inflater.inflate(R.layout.listforchoose, null);
		
		
//		setTitle("");
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		recentbooks = new ArrayList<HashMap<String, String>>();
		data=new Database(this);
		lv=(ListView)recent.findViewById(R.id.optionlist);
		setList();
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				
				// TODO Auto-generated method stub
				
				if(new File(recentbooks.get(position).get("path")).exists()){
					Calendar calendar=Calendar.getInstance();
					long time = calendar.getTimeInMillis();
					Configs.CurrentFile = recentbooks.get(position).get("path");
					data.changeTime(recentbooks.get(position).get("path"), time);
					Intent intent = new Intent(RecentBooks.this, Text.class);
					startActivity(intent);
					
				}else{
					fileChangedAlert(position);
					
				}
			}

		});
		setContentView(recent);
	}
	
	

	private void fileChangedAlert(final int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView = inflater.inflate(R.layout.aboutdialog, null);
		final TextView title = (TextView) textEntryView
				.findViewById(R.id.about_title);
		final TextView txtlink = (TextView) textEntryView
				.findViewById(R.id.content);
		title.setTypeface(Configs.UIFont);
		txtlink.setTypeface(Configs.UIFont);
		title.setText(ArabicUtilities.reshape(getString(R.string.alert)));
		txtlink.setText(ArabicUtilities
				.reshape(getString(R.string.file_moved)));
		final AlertDialog.Builder builder = new AlertDialog.Builder(RecentBooks.this);
		builder.setCancelable(false);

		builder.setView(textEntryView);
		builder.setNegativeButton(ArabicUtilities.reshape("OK"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						data.deleteOpened(recentbooks.get(position).get("path"));
						setList();
					}
				});
		builder.show();
	}


	private void setList() {
		// TODO Auto-generated method stub
		
		books = data.getRecentBooks();
		books.moveToFirst();
		for (int b=0; b<books.getCount(); b++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", books.getString(2));
			map.put("path", books.getString(1));
			recentbooks.add(map);
			books.moveToNext();
		}
		
		RecentBooksAdapter adapter=new RecentBooksAdapter(this, recentbooks);
		
		lv.setAdapter(adapter);
	}
	
}
