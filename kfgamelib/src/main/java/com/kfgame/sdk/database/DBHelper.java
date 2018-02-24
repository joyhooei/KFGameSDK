package com.kfgame.sdk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kfgame.sdk.database.DBUser.User;

/**
 *
 */
public class DBHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "kfgameuser.db";
	public static final String USER_TABLE_NAME = "user_table";
	public static final String[] USER_COLS = { User.USERNAME, User.PASSWORD, User.ISSAVED };

	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;

	public DBHelper(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	private void establishDb() {
		if (this.db == null) {
			this.db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	public long insertOrUpdate(String userName, String password, int isSaved) {
		boolean isUpdate = false;
		String[] usernames = queryAllUserName();
		for (int i = 0; i < usernames.length; i++) {
			if (userName.equals(usernames[i])) {
				isUpdate = true;
				break;
			}
		}
		long id = -1;
		if (isUpdate) {
			id = update(userName, password, isSaved);
		} else {
			if (db != null) {
				ContentValues values = new ContentValues();
				values.put(User.USERNAME, userName);
				values.put(User.PASSWORD, password);
				values.put(User.ISSAVED, isSaved);
				id = db.insert(USER_TABLE_NAME, null, values);
			}
		}
		return id;
	}


	public long delete(String userName) {
		long id = db.delete(USER_TABLE_NAME, User.USERNAME + " = '" + userName + "'", null);
		return id;
	}


	public long update(String username, String password, int isSaved) {
		ContentValues values = new ContentValues();
		values.put(User.USERNAME, username);
		values.put(User.PASSWORD, password);
		values.put(User.ISSAVED, isSaved);
		long id = db.update(USER_TABLE_NAME, values, User.USERNAME + " = '" + username + "'", null);
		return id;
	}

	public String queryPasswordByName(String username) {
		String sql = "select * from " + USER_TABLE_NAME + " where " + User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		String password = "";
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			password = cursor.getString(cursor.getColumnIndex(User.PASSWORD));
		}

		return password;
	}

	public int queryIsSavedByName(String username) {
		String sql = "select * from " + USER_TABLE_NAME + " where " + User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int isSaved = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			isSaved = cursor.getInt(cursor.getColumnIndex(User.ISSAVED));
		}
		return isSaved;
	}

	public String[] queryAllUserName() {
		if (db != null) {
			Cursor cursor = db.query(USER_TABLE_NAME, null, null, null, null,
					null, null);
			int count = cursor.getCount();
			String[] userNames = new String[count];
			if (count > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					userNames[i] = cursor.getString(cursor
							.getColumnIndex(User.USERNAME));
					cursor.moveToNext();
				}
			}
			return userNames;
		} else {
			return new String[0];
		}

	}

	public void cleanup() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + USER_TABLE_NAME + " (" + User._ID
					+ " integer primary key," + User.USERNAME + " text, "
					+ User.PASSWORD + " text, " + User.ISSAVED + " INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
			onCreate(db);
		}

	}

}
