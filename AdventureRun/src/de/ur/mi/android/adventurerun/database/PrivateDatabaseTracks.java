package de.ur.mi.android.adventurerun.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.ur.mi.android.adventurerun.data.Track;

public class PrivateDatabaseTracks {
	


	private static final String DB_NAME = "privateDatabaseTracks.db";
	private static final int DB_VERSION = 1;
	
	private static final String DB_TABLE= "tracks";
	
	private static final String KEY_ID = "_id";
	private static final String KEY_DATA = "name";
	//private static final String KEY_CHECKPOINTS = "checkpoints";
	
	private static final int COLUMN_NAME_INDEX = 1;
	private static final int COLUMN_CHECKPOINTS_INDEX = 2;
	
	private PrivateDBOpenHelper dbHelper;
	
	private SQLiteDatabase privateDB;
	
	public PrivateDatabaseTracks (Context context) {
		dbHelper = new PrivateDBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void open () throws SQLException {
		try {
			privateDB = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			privateDB = dbHelper.getReadableDatabase();
		}
		
	}
	
	public void close () {
		privateDB.close();
	}
	
	public long insertTrack (Track currentTrack) {
		ContentValues currentValues = new ContentValues();
		currentValues.put(KEY_DATA, currentTrack.getAllCheckpoints());
		
		return privateDB.insert(DB_TABLE, null, currentValues);
	}
	
	public void deleteTrack (Track currentTrack) {
		
	}
	
	//KEY ÄNDERN!!!
	public class PrivateDBOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE = "create table "
				+ DB_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " 
				+ KEY_DATA + " text not null);";

		public PrivateDBOpenHelper(Context context, String name,
				SQLiteDatabase.CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}



	}
	


}
