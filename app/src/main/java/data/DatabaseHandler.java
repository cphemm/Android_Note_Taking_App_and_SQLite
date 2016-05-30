package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Notes;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Notes> noteList = new ArrayList<>();


    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create our table
        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.TITLE_NAME +
                " TEXT, " + Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME + " LONG);";

        db.execSQL(CREATE_NOTES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        Log.v("ONUPGRADE", "DROPPING THE TABLE AND CREATING A NEW ONE!");

        //create a new one
        onCreate(db);


    }


    //delete a note
    public void deleteNote(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                new String[]{ String.valueOf(id)});

        db.close();

    }

    //add content to table
    public void addNotes(Notes note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());


        db.insert(Constants.TABLE_NAME, null, values);
        //db.insert(Constants.TABLE_NAME, null, values);

        db.close();


    }

    //Get all notes
    public ArrayList<Notes> getNotes() {

        noteList.clear();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME},null,null, null,null,Constants.DATE_NAME+ " DESC");

        //loop through cursor
        if (cursor.moveToFirst()) {

            do {

                Notes note = new Notes();
                note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
                note.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String dataData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                note.setRecordDate(dataData);

                noteList.add(note);

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return noteList;

    }


}
