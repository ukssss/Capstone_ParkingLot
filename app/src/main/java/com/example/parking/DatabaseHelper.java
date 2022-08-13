package com.example.parking;

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

public class DatabaseHelper extends SQLiteOpenHelper{

    // Error TAG
    protected static String TAG = "DatabaseHelper";

    private static String databasePath = "";
    private static String databaseName = "test.db";
    private static String tableName = "test";

    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
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

            String sql = "SELECT * FROM " + tableName;

            Cursor mCursor = mDatabase.rawQuery(sql, null);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {

                    Parkinglot parkinglot = new Parkinglot();

                    parkinglot.setId(mCursor.getInt(0));
                    parkinglot.setPrkplceNm(mCursor.getString(1));
                    parkinglot.setLnmadr(mCursor.getString(2));
                    parkinglot.setLatitude(mCursor.getDouble(3));
                    parkinglot.setLongitude(mCursor.getDouble(4));

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
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
