package com.example.parking.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParkinglotDatabaseHelper extends SQLiteOpenHelper{

    // Error TAG
    protected static String TAG = "ParkinglotDatabaseHelper";

    private static String databasePath = "";
    private static String databaseName = "parkinglot.db";
    private static String tableName = "parkinglot";

    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public ParkinglotDatabaseHelper(Context context) {
        super(context, databaseName, null, 1);

        if (Build.VERSION.SDK_INT >= 17) {
            databasePath = context.getApplicationInfo().dataDir + "/databases/";
        }
        else {
            databasePath = "/data/data/" + context.getPackageName() + "/databases/";
        }

        this.mContext = context;

    }

    public boolean OpenDatabaseFile() throws SQLException {

        if (!CheckDatabaseFileExist()) {
            CreateDatabase();
        }

        String mPath = databasePath + databaseName;

        try {
            mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            Log.e(TAG, "[SUCCESS] " + databaseName + " are Opened");
        }
        catch (SQLException sqlException) {
            Log.e(TAG, "[ERROR] " + "Can't Open Database!");
        }

        return  mDatabase != null;

    }

    public boolean CheckDatabaseFileExist() {
        File file = new File(databasePath + databaseName);
        return file.exists();
    }

    public void CreateDatabase() throws SQLException {

        this.getReadableDatabase();
        this.close();

        try {
            CopyDatabaseFile();
            Log.e(TAG, "[SUCCESS] " + databaseName + " are Created!");
        }
        catch (IOException ioException) {
            Log.e(TAG, "[ERROR] " + "Unable to create " + databaseName);
            throw new Error(TAG);
        }
    }

    public void CopyDatabaseFile() throws IOException {

        InputStream inputStream = mContext.getAssets().open(databaseName);
        String outputFileName = databasePath + databaseName;
        OutputStream outputStream = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public List getTableData() {

        try {
            List mList = new ArrayList();
            Integer arr_random_num;
            Random random = new Random();

            String sql = "SELECT * FROM " + tableName;
            Cursor mCursor = mDatabase.rawQuery(sql, null);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {

                    Parkinglot parkinglot = new Parkinglot();

                    parkinglot.setId(mCursor.getInt(0));
                    parkinglot.setDiv(mCursor.getString(1));
                    parkinglot.setName(mCursor.getString(2));
                    parkinglot.setType(mCursor.getString(3));
                    parkinglot.setAddr(mCursor.getString(4));
                    parkinglot.setOperDay(mCursor.getString(5));
                    parkinglot.setParkingchargeInfo(mCursor.getString(6));
                    parkinglot.setPhoneNumber(mCursor.getString(7));
                    parkinglot.setLatitude(mCursor.getDouble(8));
                    parkinglot.setLongitude(mCursor.getDouble(9));

                    arr_random_num = random.nextInt(50) + 1;
                    parkinglot.setParkStat(arr_random_num);

                    parkinglot.setFavorite(mCursor.getInt(11));

                    mList.add(parkinglot);
                }
            }

            return mList;

        } catch (SQLException mSQLException) {
            Log.e(TAG, mSQLException.toString());
            throw mSQLException;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public void updateFavorite(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + tableName + " SET favorite = '1' WHERE id = " + id +";";
        db.execSQL(sql);
        Log.d("Update favorite", id.toString());
    }

    public Cursor selectAllFavoriteList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + tableName + " WHERE favorite = '1';";
        return db.rawQuery(sql, null, null);
    }
}

