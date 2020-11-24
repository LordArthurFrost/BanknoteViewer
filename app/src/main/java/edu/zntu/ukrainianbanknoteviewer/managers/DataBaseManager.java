package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.HashMap;

public class DataBaseManager extends SQLiteOpenHelper
{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "banknotes.db";
    private static DataBaseManager instance;
    public SQLiteDatabase database;
    private Context context;

    public DataBaseManager(Context context)
    {
        //TODO
        super(context, "//assests/banknotes.db", null, DATABASE_VERSION);
        this.context = context;
        database = getReadableDatabase();
    }

    /*public static DataBaseManager getInstance(Context context) {
        if (instance == null) {
            String path = context.getObbDir().getPath() + "/" + DATABASE_NAME;

            File file = new File(path);
            if (!(file.exists() && !file.isDirectory()))
                return null;

            instance = new DataBaseManager(context, path);
        }
        return instance;
    }*/


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //NULL
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //NULL
    }
}
