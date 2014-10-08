package de.ur.mi.android.adventurerun.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.ur.mi.android.adventurerun.data.Track;

public class PrivateDatabaseScores {
	
	private final String DB_NAME = "privateDatabaseScores.db";
	private final int DB_VERSION = 1;

	private final String DB_TABLE = "scores";

	private final String KEY_ID = "_id";
	private final String KEY_NAME = "name";
	private final String KEY_TIMESTAMP = "timestamp";
	private final String KEY_FIRST_SCORE = "firstScore";
	private final String KEY_SECOND_SCORE = "secondScore";
	private final String KEY_THIRD_SCORE = "thirdScore";
	private final String KEY_FOURTH_SCORE = "fourthScore";
	private final String KEY_FIFTH_SCORE = "fifthScore";

	private final int COLUMN_NAME_INDEX = 1;
	private final int COLUMN_TIMESTAMP_INDEX = 2;
	private final int COLUMN_FIRST_SCORE_INDEX = 3;
	private final int COLUMN_SECOND_SCORE_INDEX = 4;
	private final int COLUMN_THIRD_SCORE_INDEX = 5;
	private final int COLUMN_FOURTH_SCORE_INDEX = 6;
	private final int COLUMN_FIFTH_SCORE_INDEX = 7;

	private PrivateDBOpenHelper dbHelper;

	private SQLiteDatabase privateDB;

	public PrivateDatabaseScores(Context context) {
		dbHelper = new PrivateDBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}

	public void open() throws SQLException {
		try {
			privateDB = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			privateDB = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		privateDB.close();
	}

	public long createNewScoreList(Track currentTrack) {
		ContentValues currentValues = new ContentValues();
		currentValues.put(KEY_NAME, currentTrack.getName());
		currentValues.put(KEY_TIMESTAMP, currentTrack.getTimestamp());
		currentValues.put(KEY_FIRST_SCORE, 0);
		currentValues.put(KEY_SECOND_SCORE, 0);
		currentValues.put(KEY_THIRD_SCORE, 0);
		currentValues.put(KEY_FOURTH_SCORE, 0);
		currentValues.put(KEY_FIFTH_SCORE, 0);
		
		return privateDB.insert(DB_TABLE, null, currentValues);
	}
	
	public void updateScore(Track currentTrack, long time) {
		Cursor cursor = privateDB.query(DB_TABLE, new String [] { KEY_ID, KEY_NAME, KEY_TIMESTAMP,
				KEY_FIRST_SCORE, KEY_SECOND_SCORE, KEY_THIRD_SCORE, KEY_FOURTH_SCORE, KEY_FIFTH_SCORE
				}, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			while (true) {
				
				String name = cursor.getString (COLUMN_NAME_INDEX);
				long timestamp = cursor.getLong (COLUMN_TIMESTAMP_INDEX);
				
				if (name.equals(currentTrack.getName()) && timestamp == currentTrack.getTimestamp()) {
					
					if (time < cursor.getLong(COLUMN_FIRST_SCORE_INDEX)) {
						insertNewScore(KEY_FIRST_SCORE, time, currentTrack);
					} else if (time < cursor.getLong(COLUMN_SECOND_SCORE_INDEX)) {
						insertNewScore(KEY_SECOND_SCORE, time, currentTrack);
					} else if (time < cursor.getLong(COLUMN_THIRD_SCORE_INDEX)) {
						insertNewScore(KEY_THIRD_SCORE, time, currentTrack);
					} else if (time < cursor.getLong(COLUMN_FOURTH_SCORE_INDEX)) {
						insertNewScore(KEY_FOURTH_SCORE, time, currentTrack);
					} else if (time < cursor.getLong(COLUMN_FIFTH_SCORE_INDEX)) {
						insertNewScore(KEY_FIFTH_SCORE, time, currentTrack);
					}
					break;
				}
				
				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
	}

	private void insertNewScore(String key, long time, Track currentTrack) {
		String updateClause = KEY_TIMESTAMP + "=?";
		String[] updateArgs = new String[] { String.valueOf(currentTrack.getTimestamp()) };
		
		ContentValues currentValues = new ContentValues();	
		currentValues.put(key, time);
		privateDB.update(DB_TABLE, currentValues, updateClause, updateArgs);
	}


	public void deleteScoreList(Track currentTrack) {
		String deleteClause = KEY_TIMESTAMP + "=?";
		String[] deleteArgs = new String[] { String.valueOf(currentTrack
				.getTimestamp()) };

		privateDB.delete(DB_TABLE, deleteClause, deleteArgs);
	}
	
	public void updateName(Track currentTrack) {
		String updateClause = KEY_TIMESTAMP + "=?";
		String[] updateArgs = new String[] { String.valueOf(currentTrack.getTimestamp()) };
		
		ContentValues currentValues = new ContentValues();
		currentValues.put(KEY_NAME, currentTrack.getName());
		privateDB.update(DB_TABLE, currentValues, updateClause, updateArgs);
	}


	public long[] getScoreList(Track currentTrack) {
		long [] scoreList = new long[5];
		Cursor cursor = privateDB.query(DB_TABLE, new String [] { KEY_ID, KEY_NAME, KEY_TIMESTAMP,
				KEY_FIRST_SCORE, KEY_SECOND_SCORE, KEY_THIRD_SCORE, KEY_FOURTH_SCORE, KEY_FIFTH_SCORE
				}, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			while (true) {
				long timestamp = cursor.getLong(COLUMN_TIMESTAMP_INDEX);
				String name = cursor.getString(COLUMN_NAME_INDEX);

				if (name.equals(currentTrack.getName()) && timestamp == currentTrack.getTimestamp()) {
					scoreList [0] = cursor.getLong(COLUMN_FIRST_SCORE_INDEX);
					scoreList [1] = cursor.getLong(COLUMN_SECOND_SCORE_INDEX);
					scoreList [2] = cursor.getLong(COLUMN_THIRD_SCORE_INDEX);
					scoreList [3] = cursor.getLong(COLUMN_FOURTH_SCORE_INDEX);
					scoreList [4] = cursor.getLong(COLUMN_FIFTH_SCORE_INDEX);
					break;
				}

				if (cursor.moveToNext() == false) {
					break;
				}
			}
		}
		return scoreList;
	}


	public class PrivateDBOpenHelper extends SQLiteOpenHelper {
		private final String DATABASE_CREATE = "create table " + DB_TABLE
				+ " (" + KEY_ID + " integer primary key autoincrement, "
				+ KEY_NAME + " text, " + KEY_TIMESTAMP + " integer, "
				+ KEY_FIRST_SCORE + " integer, "
				+ KEY_SECOND_SCORE + " integer, "
				+ KEY_THIRD_SCORE + " integer, "
				+ KEY_FOURTH_SCORE + " integer, "
				+ KEY_FIFTH_SCORE + " integer);";

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
