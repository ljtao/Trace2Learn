package edu.upenn.cis350;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class CharactersDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_FILENAME, MySQLiteHelper.COLUMN_TAGS };

	public CharactersDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createCharacter(String name, String filename, String tag) {
		/*ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_FILENAME, filename);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_TAGS, tag);*/
		
		database.execSQL("INSERT INTO " + dbHelper.TABLE_CHARACTERS
							+ "( " + MySQLiteHelper.COLUMN_ID 
							+ MySQLiteHelper.COLUMN_NAME
							+ MySQLiteHelper.COLUMN_FILENAME
							+ MySQLiteHelper.COLUMN_TAGS + ") " 
							+ "VALUES (NULL, + '" + name + "', '" + filename + "', '" + tag + "');");
		// To show how to query
		/*Cursor cursor = database.query(MySQLiteHelper.TABLE_CHARACTERS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		return cursorToCharacter(cursor);*/
	}

	public void deleteCharacter(UserCharacter name) {
		long id = name.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_CHARACTERS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<UserCharacter> getAllCharacters() {
		List<UserCharacter> uc_list = new ArrayList<UserCharacter>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CHARACTERS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserCharacter character = cursorToCharacter(cursor);
			uc_list.add(character);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return uc_list;
	}

	private UserCharacter cursorToCharacter(Cursor cursor) {
		UserCharacter uc = new UserCharacter();
		uc.setId(cursor.getLong(0));
		uc.setName(cursor.getString(1));
		uc.setImagePath(cursor.getString(2));
		uc.setTags(cursor.getString(3));
		return uc;
	}
}

