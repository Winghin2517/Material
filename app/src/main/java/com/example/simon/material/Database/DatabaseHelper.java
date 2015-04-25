package com.example.simon.material.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simon.material.Model.Place;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Simon on 2015/04/12.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String PLACES_TABLE_NAME = "Places";
    public static final String PLACES_COLUMN_ID = "id";
    public static final String PLACES_COLUMN_PLACENAME = "name";
    public static final String PLACES_COLUMN_PLACEDESC = "description";
    public static final String PLACES_COLUMN_PICURL = "url";
    //I might have to add GPS co-ords at one point in time

    private static final String[] COLUMNS = {PLACES_COLUMN_ID, PLACES_COLUMN_PLACENAME, PLACES_COLUMN_PLACEDESC, PLACES_COLUMN_PICURL};

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PlacesDB";

    //This constructor will only let me create the places database - it will not let me create other types of DBs.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + PLACES_TABLE_NAME +
                        " (" + PLACES_COLUMN_ID + " integer primary key AUTOINCREMENT, " +
                        PLACES_COLUMN_PLACENAME + " text, " +
                        PLACES_COLUMN_PLACEDESC + " text, " +
                        PLACES_COLUMN_PICURL + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLACES_TABLE_NAME);
        onCreate(db);
    }

    public void addPlace(Place place) {
        //for logging
        //Log.d("addBook", book.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PLACES_COLUMN_PLACENAME, place.getName_of_place()); // get title
        values.put(PLACES_COLUMN_PLACEDESC, place.getDescription_place());
        values.put(PLACES_COLUMN_PICURL, place.getPic_url()); // get author

        // 3. insert
        db.insert(PLACES_TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Place getPlace(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(PLACES_TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Place place = new Place();
        place.setId(Integer.parseInt(cursor.getString(0)));
        place.setName_of_place(cursor.getString(1));
        place.setDescription_place(cursor.getString(2));
        place.setPic_url(cursor.getString(3));

        //log
        //Log.d("getBook(" + id + ")", book.toString());

        // 5. return places
        return place;
    }

    public List<Place> getAllPlaces() {
        List<Place> placeList = new LinkedList<Place>();

        // 1. build the query
        String query = "SELECT  * FROM " + PLACES_TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Place place = null;
        if (cursor.moveToFirst()) {
            do {
                place = new Place();
                place.setId(Integer.parseInt(cursor.getString(0)));
                place.setName_of_place(cursor.getString(1));
                place.setDescription_place(cursor.getString(2));
                place.setPic_url(cursor.getString(3));

                // Add book to books
                placeList.add(place);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllBooks()", books.toString());

        // return books
        return placeList;
    }

    public int updatePlace(Place place) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PLACES_COLUMN_PLACENAME, place.getName_of_place()); // get place name
        values.put(PLACES_COLUMN_PLACEDESC, place.getDescription_place());
        values.put(PLACES_COLUMN_PICURL, place.getPic_url()); // get pic url

        // 3. updating row
        int i = db.update(PLACES_TABLE_NAME, //table
                values, // column/value
                PLACES_COLUMN_ID + " = ?", // selections
                new String[]{String.valueOf(place.getId())}); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single place
    public void deletePlace(Place place) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(PLACES_TABLE_NAME,
                PLACES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(place.getId())});

        // 3. close
        db.close();

        //Log.d("deleteBook", place.toString());

    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + PLACES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;

    }

    public void deleteAllPlaces() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ PLACES_TABLE_NAME);
        db.close();
    }
}