package es.upv.sdm.labs.bikeroutes.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "bike_routes";

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableEvent = "CREATE TABLE event ("+
                "id INTEGER PRIMARY KEY NOT NULL,"+
                "type_id INTEGER,"+
                "date TEXT,"+
                "departure_id INTEGER,"+
                "arrival_id INTEGER,"+
                "description TEXT,"+
                "secret INTEGER,"+
                "organizer_id INTEGER,"+
                "over INTEGER"+
                ");";
        String tableEventType = "CREATE TABLE event_type (" +
                "  id INTEGER PRIMARY KEY NOT NULL," +
                "  type INTEGER," +
                "  specification TEXT" +
                "); ";
        String tableLocation = "CREATE TABLE location (" +
                "  id INTEGER PRIMARY KEY NOT NULL," +
                "  latitude REAL," +
                "  longitude REAL," +
                "  address TEXT" +
                ");";
        String tableUser = "CREATE TABLE user (" +
                "  id INTEGER PRIMARY KEY NOT NULL," +
                "  name TEXT," +
                "  mail TEXT," +
                "  password TEXT," +
                "  description TEXT," +
                "  gender INTEGER," +
                "  image BLOB" +
                "); ";
        String insert1 = "INSERT INTO event_type VALUES (1, 0, 'BIKE')";
        String insert2 = "INSERT INTO event_type VALUES (2, 1, 'HIKE')";
        String insert3 = "INSERT INTO event_type VALUES (3, 2, 'RUN')";
        db.execSQL(tableEvent);
        db.execSQL(tableEventType);
        db.execSQL(tableLocation);
        db.execSQL(tableUser);
        db.execSQL(insert1);
        db.execSQL(insert2);
        db.execSQL(insert3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
