package de.ur.mi.android.adventurerun.database;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import de.ur.mi.android.adventurerun.data.Checkpoint;
import de.ur.mi.android.adventurerun.data.Track;

public class PrivateDatabaseTracks {
	


	private static final String DB_NAME = "privateDatabaseTracks.db";
	private static final int DB_VERSION = 1;
	
	private static final String DB_TABLE= "tracks";
	
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CHECKPOINTS = "checkpoints";
	
	private static final int COLUMN_TIMESTAMP_INDEX = 0;
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
		currentValues.put(KEY_ID, currentTrack.getTimestamp());
		currentValues.put(KEY_CHECKPOINTS, currentTrack.getAllCheckpointsJSON());
		
		return privateDB.insert(DB_TABLE, null, currentValues);
	}
	
	public void deleteTrack (Track currentTrack) {
		String deleteClause = KEY_ID + "=?";
		String [] deleteArgs = new String [] {String.valueOf(currentTrack.getTimestamp())};
		
		privateDB.delete(DB_TABLE, deleteClause, deleteArgs);
	}
	

	// Noch nicht abgeschlossen: Reichen latitude und altitude als Daten für den Checkpoint aus?
	public ArrayList<Track> allTracks() {
		ArrayList <Track> tracksArray = new ArrayList<Track>();
		Cursor cursor = privateDB.query(DB_TABLE, new String [] { KEY_ID, KEY_NAME, KEY_CHECKPOINTS}, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			while (true) {
				long timestamp = cursor.getLong(COLUMN_TIMESTAMP_INDEX);
				String name = cursor.getString(COLUMN_NAME_INDEX);
				String checkpoints = cursor.getString(COLUMN_CHECKPOINTS_INDEX);
				
				ArrayList <Checkpoint> checkpointList = parseCheckpointsOfDB (checkpoints);
				Track currentTrack = new Track (checkpointList, name, timestamp);
				tracksArray.add(currentTrack);
				if (cursor.moveToNext() == false) {
					break;
				}
			}
			
		}
		return tracksArray;
	}
	
	
	private ArrayList<Checkpoint> parseCheckpointsOfDB(String checkpoints) {
		ArrayList <Checkpoint> checkpointList = new ArrayList<Checkpoint>();
		try {
			JSONObject jsonString = new JSONObject(checkpoints);
			JSONArray jsonArray =  jsonString.getJSONArray("allCheckpoints");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return checkpointList;
	}


	public class PrivateDBOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE = "create table "
				+ DB_TABLE + " (" + KEY_ID + " integer primary key, " 
				+ KEY_NAME + " text, " + KEY_CHECKPOINTS + " text not null);";

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
