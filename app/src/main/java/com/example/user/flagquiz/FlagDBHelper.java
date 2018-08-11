package com.example.user.flagquiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FlagDBHelper extends SQLiteOpenHelper {

    private Resources mResources;
    public FlagDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }
    private static final String DATABASE_NAME = "Flags.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Flags";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "Code";
    public static final String COLUMN_NAME = "Name";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CODE + " TEXT,"
                    + COLUMN_NAME + " TEXT"
                    + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
            readFlagsFromResources(db);
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create table again
        onCreate(db);
    }

    private void readFlagsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.data_json);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        //Parse JSON data and insert into the provided database instance

        //JSONObject root = new JSONObject(rawJson);
        JSONArray flags = new JSONArray(rawJson);

        //put flags into ContentValue
        ContentValues values = new ContentValues();
        //enclose in try/catch block to handle error exceptions
        try {
            for (int i = 0; i < flags.length(); i++) {
                JSONObject item = flags.getJSONObject(i);

                values.put(COLUMN_CODE,item.getString("Code"));
                values.put(COLUMN_NAME, item.getString("Name"));

                db.insert(TABLE_NAME, null, values);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public Cursor queryFlags(){

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
