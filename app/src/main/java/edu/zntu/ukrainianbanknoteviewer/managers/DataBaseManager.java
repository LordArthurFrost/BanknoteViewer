package edu.zntu.ukrainianbanknoteviewer.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class DataBaseManager extends SQLiteOpenHelper
{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "banknotes.db";
    private static String DATABASE_PATH;
    private static SQLiteDatabase sqLiteDatabase;
    public static Context context;
    private static Cursor cursor;


    public DataBaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getFilesDir().getPath() + DATABASE_NAME;
        create_db();
        open();
    }

    public static Cursor check()
    {
        cursor = sqLiteDatabase.rawQuery("select *" +
                "from main", null);
        cursor.moveToFirst();
        while(cursor.moveToNext())
        Log.d("Database", cursor.getString(cursor.getColumnIndex("denomination")));
        return cursor;
    }

    void create_db()
    {
        InputStream inputStream;
        OutputStream outputStream;
        Log.d("Database","Checking creation");
        try
        {

            File file = new File(DATABASE_PATH);
            if (!file.exists())
            {
                Log.d("Database","Creating Database");
                this.getReadableDatabase();
                //получаем локальную бд как поток
                inputStream = context.getAssets().open(DATABASE_NAME);
                // Путь к новой бд
                String outFileName = DATABASE_PATH;

                // Открываем пустую бд
                outputStream = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException ex)
        {
            Log.d("Database", ex.getMessage());
        }
    }

    public void open() throws SQLException
    {

        this.sqLiteDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Log.d("Database","Opened Successfully");
    }

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


