package net.uyghurdev.uyghurtextreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

	private final static String DB_PATH = "/data/data/net.uyghurdev.uyghurtextreader/databases/";
	private static final String DATABASE_NAME = "textreader";
	private final static int DATABASE_VERSION = 3;
	
	SQLiteDatabase db;
	Cursor cursor;
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	
		db.execSQL("CREATE TABLE recentbooks(_id INTEGER PRIMARY KEY AUTOINCREMENT,filePath TEXT, fileName TEXT, time DATETIME);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public Cursor getRecentBooks() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("recentbooks", null,null, null, null, null, "time DESC");
		return cursor;
	}

	public void changeTime(String path, long time) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		String[] args={path};
		ContentValues cv = new ContentValues();
		cv.put("time", time);
		db.update("recentbooks", cv, "filePath=?", args);
	}

	public void deleteOpened(String path) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase(); 
	    String[] args={path};
	    db.delete("recentbooks", "filePath=?", args);
	}

	public Cursor getRecent(String filePath) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		String[] args={filePath};
		Cursor cursor = db.query("recentbooks", null,"filePath=?", args, null, null, null);
		return cursor;
	}

	public void addRecent(String filePath, String name, long time) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("filePath", filePath);
		cv.put("fileName", name);
		cv.put("time", time);
		db.insert("recentbooks", "filePath", cv);
	}
	

}
